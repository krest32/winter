//gitlab的凭证
def git_auth = "36a8360b-3146-4bb6-8d6a-d3ef39be5d57"
//gitLab SSH地址
def git_url= "https://gitee.com/krest202/winter.git"

def tag = "latest"
def ali_url = "registry.cn-qingdao.aliyuncs.com"
def ali_project = "krest-winter"
def ali_auth = "2a4e634f-3a70-47a3-889f-ed4b1e802b5e"

node {
    //获取project_name变量，得到当前项目的名称,split根据(,)切割每个项目名的，会被切割成为一个数组
    def selectedProjectNames = "${project_name}".split(",")
    def selectedServer = "${publish_server}".split(",")
    stage('删除旧的镜像文件') {
         sshPublisher(publishers: [sshPublisherDesc(configName: 'ali_server',transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/opt/jenkins_shell/removeImage.sh", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
        }
    stage('拉取代码') {
        //分支的变量参数
        checkout([$class: 'GitSCM', branches: [[name: "*/${branch}"]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [],
            submoduleCfg: [],
            //登陆凭证
            userRemoteConfigs: [[credentialsId: "${git_auth}", url:"${git_url}"]]])
    }
    stage('安装编译公共子工程') {
            //将公共类放入到仓库中，所以使用install指令
            sh "mvn -f common clean install"
        }
    stage('打包服务项目,生成镜像,上传镜像') {
        //对获取project_name变量，得到当前项目的名称,split根据(,)切割每个项目名的，会被切割成为一个数组，进行for循环
        for(int i=0; i<selectedProjectNames.length; i++){
            //每遍历一次，会得到项目的名字和端口
            def projectInfo = selectedProjectNames[i];
            //数组下标为0，得到项目名字
            def currentProjectName = "${projectInfo}".split("@")[0];
            //数组下标为0，得到项目端口
            def currentProjectPort = "${projectInfo}".split("@")[1];
            // 打包并且生成镜像文件
            sh "mvn -f ${currentProjectName} clean package dockerfile:build"
            //定义镜像名称
            def imageName = "${currentProjectName}:${tag}"
            //打标签
            sh "docker tag ${imageName}  ${ali_url}/${ali_project}/${imageName}"
            withCredentials([usernamePassword(credentialsId: "${ali_auth}", passwordVariable: 'password', usernameVariable: 'username')]) {
               //登录到Harbor
               sh "docker login -u ${username} -p ${password} ${ali_url}"
               //镜像上传
               sh "docker push ${ali_url}/${ali_project}/${imageName}"
               sh "echo 镜像上传成功"
            }
        }
    }

    stage('部署应用') {
        //部署应用
        for(int i=0;i < selectedProjectNames.length;i++){
            //每遍历一次，会得到项目的名字和端口
            def projectInfo = selectedProjectNames[i];
            //数组下标为0，得到项目名字
            def currentProjectName = "${projectInfo}".split("@")[0]
            //数组下标为0，得到项目端口
            def currentProjectPort = "${projectInfo}".split("@")[1]
            for(int j=0;j<selectedServer.length;j++){
                def currentServerName =selectedServer[j]
                //加上参数选择不同的服务器
                sh "echo 部署应用 ${currentServerName} ${currentProjectName} ${currentProjectPort}"
                sshPublisher(publishers: [sshPublisherDesc(configName: "${currentServerName}", transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/opt/jenkins_shell/deploy.sh $ali_url $ali_project $currentProjectName $tag $currentProjectPort", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
                sh "echo 部署应用成功"
            }
        }
    }

}