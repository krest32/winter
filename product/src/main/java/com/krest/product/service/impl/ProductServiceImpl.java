package com.krest.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.krest.product.client.AliyunClient;
import com.krest.product.entity.*;
import com.krest.product.entity.vo.ProductAttrVo;
import com.krest.product.entity.vo.QueryPuoductVo;
import com.krest.product.entity.vo.ResultQueryProductVo;
import com.krest.product.mapper.ProductMapper;
import com.krest.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.krest.utils.entity.EsAttr;
import com.krest.utils.entity.EsProductModel;
import com.krest.utils.myexception.myException;
import com.krest.utils.response.R;
import com.krest.utils.utils.IdWorker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author krest
 * @since 2020-12-11
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private CatelogService catelogService;

    @Autowired
    private BrandSortService brandSortService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CatelogAttrgroupService catelogAttrgroupService;

    @Autowired
    private CatelogAttrService catelogAttrService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AliyunClient aliyunClient;

    @Autowired
    private StockService stockService;

    public static IdWorker idWorker = new IdWorker();

    @Override
    public ResultQueryProductVo pageQueryAllProduct(Long page, Long limit, QueryPuoductVo queryPuoductVo) {
        //准备工作
        QueryWrapper<Product> productQueryWrapper = new QueryWrapper<>();
        Page<Product> productPage = new Page<>(page,limit);
        String title = queryPuoductVo.getTitle();
        String bandId = queryPuoductVo.getBandId();
        String sortId = queryPuoductVo.getSortId();

        // 通过日期降序排序
        productQueryWrapper.orderByDesc("gmt_modified");

        if (!StringUtils.isEmpty(title)){
            productQueryWrapper.like("title",title);
        }
        if (!StringUtils.isEmpty(bandId)){
            productQueryWrapper.eq("brand_id",bandId);
        }
        if (!StringUtils.isEmpty(sortId)){
            productQueryWrapper.eq("one_sort_id",sortId);
        }
        IPage<Product> productIPage = baseMapper.selectPage(productPage, productQueryWrapper);
        ResultQueryProductVo resultQueryProductVo = new ResultQueryProductVo();
        List<Product> records = productIPage.getRecords();


        // 封转数据
        List<Brand> brandList = brandService.list(null);
        List<Catelog> catelogList = catelogService.list(null);

        List<Product> collect = records.stream().map(record->{
            // 封转品牌信息
            return editProductBrandInfo(record,brandList);
        }).map(record->{
            //封装产品分类信息
            return editProductSortInfo(record,catelogList);
        }).map(record->{
            // 封转产品属性信息
            return editProductAttrGroupInfo(record);
        }).collect(Collectors.toList());


        //设置返回结果
        resultQueryProductVo.setTotal(productIPage.getTotal());
        resultQueryProductVo.setProductList(collect);


        return resultQueryProductVo;
    }

    @Override
    public void addNewProduct(Product product) {
        product.setId(idWorker.nextId());
        if(StringUtils.isEmpty(product.getPictureName())){
            throw new myException(20001,"请添加图片信息");
        }
        baseMapper.insert(product);
    }

    @Override
    public Product getProductById(String id) {
        Product product = baseMapper.selectById(id);

        // 封转数据
        List<Brand> brandList = brandService.list(null);
        List<Catelog> catelogList = catelogService.list(null);

        // 封转品牌信息
        Product product1 = editProductBrandInfo(product, brandList);

        //封装产品分类信息
        Product product2 = editProductSortInfo(product1, catelogList);

        // 封转产品属性信息
        Product product3 = editProductAttrGroupInfo(product2);

        return product3;
    }

    @Override
    public void updateProduct(Product product) {
        String id = product.getId();
        // 校验图片信息
        Product selectById = baseMapper.selectById(id);
        String pictureName = selectById.getPictureName();
        if (!(pictureName.equals(product.getPictureName()))){
            aliyunClient.deleteOss(selectById.getPictureName());
        }

        // 校验产品分类是否改变
        String oneSortId = selectById.getOneSortId();
        if (!(oneSortId.equals(product.getOneSortId()))){
            QueryWrapper<Attr> queryWrapper =new QueryWrapper<>();
            queryWrapper.eq("product_id",product.getId());
            attrService.remove(queryWrapper);
        }

        baseMapper.updateById(product);
    }

    @Override
    public void deleteProductById(String id) {
        Product product = baseMapper.selectById(id);

        //删除图片
        aliyunClient.deleteOss(product.getPictureName());

        //删除属性信息
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("product_id",product.getId());
        attrService.remove(attrQueryWrapper);

        //删除商品信息
        baseMapper.deleteById(id);
    }

    @Override
    public R changeProductStatus(String id) {
        Product product = baseMapper.selectById(id);
        Boolean status = product.getStatus();
        if (status){
            status=false;
        }else {
            status=true;
        }
        product.setStatus(status);
        baseMapper.updateById(product);
        return R.ok();
    }

    @Override
    public R getEsProductInfo(String id) {
        EsProductModel esProductInfo = new EsProductModel();
        Product product = this.getProductById(id);

        // 获取库存xinxi
        List<Stock> stockByProductId = stockService.getStockByProductId(product.getId());
        Long stock = stockByProductId.get(0).getStock();


        esProductInfo.setId(product.getId());
        esProductInfo.setTitle(product.getTitle());

        esProductInfo.setBrandId(product.getBrandId());
        esProductInfo.setBrandTitle(product.getBrandTitle());

        esProductInfo.setOneSortId(product.getOneSortId());
        esProductInfo.setOneSortTitle(product.getOneSortTitle());

        esProductInfo.setTwoSortId(product.getTwoSortId());
        esProductInfo.setTwoSortTitle(product.getTwoSortTitle());

        esProductInfo.setThreeSortId(product.getThreeSortId());
        esProductInfo.setThreeSortTitle(product.getThreeSortTitle());

        esProductInfo.setStatus(product.getStatus());

        if (stock==0){
            esProductInfo.setHasStock(false);
        }else {
            esProductInfo.setHasStock(true);
        }

        esProductInfo.setStock(stock);

        esProductInfo.setPrice(product.getPrice());

        esProductInfo.setPicture(product.getPicture());
        esProductInfo.setPictureName(product.getPictureName());

        List<Attr> attrList = product.getAttrList();
        List<EsAttr> esAttrs = new ArrayList<>();

        for (Attr attr : attrList) {
            EsAttr esAttr = new EsAttr();
            esAttr.setAttrId(attr.getAttrId());
            esAttr.setAttrTitle(attr.getAttrTitle());
            esAttr.setAttrGroupId(attr.getAttrGroupId());
            esAttr.setAttrGroupTitle(attr.getAttrGroupTitle());
            esAttrs.add(esAttr);
        }
        esProductInfo.setAttrList(esAttrs);

        //返回需要的EsProduct信息
        return R.ok().data("esProduct",esProductInfo);
    }

    @Override
    public R listNewProducts() {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_modified");
        queryWrapper.last("limit 8");
        List<Product> products = baseMapper.selectList(queryWrapper);


        // 封转数据
        List<Brand> brandList = brandService.list(null);
        List<Catelog> catelogList = catelogService.list(null);

        List<Product> collect = products.stream().map(record->{
            // 封转品牌信息
            return editProductBrandInfo(record,brandList);
        }).map(record->{
            //封装产品分类信息
            return editProductSortInfo(record,catelogList);
        }).map(record->{
            // 封转产品属性信息
            return editProductAttrGroupInfo(record);
        }).collect(Collectors.toList());


        return R.ok().data("collect",collect);
    }

    private Product editProductAttrGroupInfo(Product record) {
        String recordId = record.getId();
        List<CatelogAttrgroup> attrgroupList = catelogAttrgroupService.list(null);

        // 得到所有属性关系表信息
        QueryWrapper<Attr> attrQueryWrapper = new QueryWrapper<>();
        attrQueryWrapper.eq("product_id",recordId);
        List<Attr> attrList = attrService.list(attrQueryWrapper);

        // 封装 AttrGroupIdList 与AttrIdList
        List<String> attrGroupIdList = new ArrayList<>();
        List<String> attrIdList = new ArrayList<>();

        for (Attr attr : attrList) {
            String attrGroupId = attr.getAttrGroupId();
            String attrId = attr.getAttrId();
            attrIdList.add(attrId);
            attrGroupIdList.add(attrGroupId);
        }
        record.setAttrIdList(attrIdList);
        record.setAttrGroupIdList(attrGroupIdList);


        // 封装产品属性
        List<Attr> attrsList = attrList.stream().map(attr -> {
            return editAttrInfo(attr,attrgroupList);
        }).collect(Collectors.toList());


        record.setAttrList(attrsList);

        // 封装分组属性信息合集
        List<ProductAttrVo> productAttrVoList = new ArrayList<>();
        for (String attrGroupId : attrGroupIdList) {
            ProductAttrVo productAttrVo = new ProductAttrVo();
            //封装GroupId
            productAttrVo.setAttrGroupId(attrGroupId);
            // 封转Group AttrList
            List<Attr> groupAttrs = new ArrayList<>();
            for (Attr attr : attrsList) {
                String groupId = attr.getAttrGroupId();

                if (attrGroupId.equals(groupId)){
                    productAttrVo.setAttrGroupId(attr.getAttrGroupTitle());
                    groupAttrs.add(attr);
                }
            }

            productAttrVo.setAttrList(groupAttrs);
            productAttrVoList.add(productAttrVo);
        }

        record.setProductAttrList(productAttrVoList);

        return record;
    }

    private Attr editAttrInfo(Attr attr, List<CatelogAttrgroup> attrgroupList) {
        List<CatelogAttr> catelogAttrs = catelogAttrService.list(null);
        for (CatelogAttrgroup catelogAttrgroup : attrgroupList) {
            String attrgroupId = catelogAttrgroup.getId();
            if (attrgroupId.equals(attr.getAttrGroupId())){
                attr.setAttrGroupTitle(catelogAttrgroup.getTitle());
            }
        }

        for (CatelogAttr catelogAttr : catelogAttrs) {
            String id = catelogAttr.getId();
            if (id.equals(attr.getAttrId())){
                attr.setAttrTitle(catelogAttr.getTitle());
            }
        }
        return attr;
    }

    private Product editProductSortInfo(Product record, List<Catelog> catelogList) {
        // 封转一级分类信息
        for (Catelog catelog : catelogList) {
            String catelogId = catelog.getId();
            String oneSortId = record.getOneSortId();
            if(oneSortId.equals(catelogId)){
                record.setOneSortTitle(catelog.getTitle());
                record.setTwoSortId(catelog.getParentId());
            }
        }

        // 封转二级分类信息
        for (Catelog catelog : catelogList) {
            String catelogId = catelog.getId();
            String twoSortId = record.getTwoSortId();
            if (catelogId.equals(twoSortId)){
                record.setTwoSortTitle(catelog.getTitle());
                record.setThreeSortId(catelog.getParentId());
            }
        }

        // 封转三级分类信息
        for (Catelog catelog : catelogList) {
            String catelogId = catelog.getId();
            String threeSortId = record.getThreeSortId();
            if (catelogId.equals(threeSortId)){
                record.setThreeSortTitle(catelog.getTitle());
            }
        }

        // 封装 SortId 集合
        List<String> idList = new ArrayList<>();
        idList.add(record.getThreeSortId());
        idList.add(record.getTwoSortId());
        idList.add(record.getOneSortId());
        record.setSortIdList(idList);

        return record;
    }

    private Product editProductBrandInfo(Product record, List<Brand> brandList) {
        for (Brand brand : brandList) {
            String brandId = brand.getId();
            String recordBrandId = record.getBrandId();
            if (brandId.equals(recordBrandId)){
                record.setBrandTitle(brand.getTitle());
            }
        }
        return record;
    }
}
