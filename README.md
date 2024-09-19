![KryoNet](https://raw.github.com/wiki/EsotericSoftware/kryonet/images/logo.jpg)

## Re-NotTiled-Server
这是一个基于 WandMire 的 jar 重构的协作式开放服务包。
它基于 Kryonet 技术。     
目前，该程序与 NotTiled 可正常工作。    
使用此项目，必须保证Main.java 的 TCP和UDP与客户端的保持一致，     
否则就会连接出错！！！

## 项目依赖和结构
- 代码：Java  
- 版本：JDK-11   
- 依赖：Kryonet  
- 架构：Gradle KTS   

## 未来方向
- [x] 反编译代码迁移
- [x] 重构/优化 项目代码
- [x] 成功连接 NotTiled 并能使用
- [ ] 更多新的合作改进

## 如何使用
1. 下载 IDEA 并克隆项目。
2. 自动同步后，构建工件（Build Artifact)
3. 运行打包的jar，例如 java -jar Server.jar
4. 通过 NotTiled 连接到服务器，实现合作
