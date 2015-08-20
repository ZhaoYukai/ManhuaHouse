# ManhuaHouse
<br>
【Android项目】在线观看漫画的App，使用聚合数据提供的“漫画书”相关的API接口<br>
<br>
####本项目的聚合数据 openid 和 key 不提供，可以去聚合数据自己申请漫画书的SDK。<br>
<br>
本项目由于用到了TabPageIndicator，所以需要引入一个名叫viewPagerlibrary的外部库<br>
项目地址为：https://github.com/ZhaoYukai/viewPagerlibrary<br>
把该项目import进Eclipse后，按照图示配置即可。<br>
<br>
####本项目涉及的知识点：<br>
<br>
（1）使用TabPageIndicator + ViewPager实现顶部Tab滑动切换效果。<br>
<br>
~~（2）使用三级缓存机制来加载并显示图片。~~<br>
~~1.第一层缓存是网络层：根据聚合数据的API传递参数，解析返回的JSON数据，从中提取出~~<br>
~~漫画相关的图片的网址URL，然后根据这个URL创建网络连接获取到图片的输出流。~~<br>
~~2.第二层是使用DiskLruCache实现的在Android手机的硬盘上（或者说是SD卡）缓存图片。~~<br>
~~程序从网络上下载到图片后，会使用DiskLruCache的硬盘缓存机制，把图片的URL作为key写入~~<br>
~~到硬盘里，并把图片的资源添加到内存中。~~<br>
~~3.第三层是使用LruCache实现的内存层。~~<br>
~~图片在加载的时候，首先会检测内存中是否有图片的资源，如果有就直接加载；如果内存中没有~~<br>
~~就去硬盘里面去找，如果找到了就添加进内存中。如果硬盘中也没有就开启一个AsyncTask异步~~<br>
~~任务从网络中去加载。~~<br>
<br>
（2）由于上面删除线中使用的三级缓存的方法发生了比较大的OOM问题，所以我进行了全面的更新，<br>
使用了著名的图片加载开源库Universal-Image-Loader，极大地提高了图片加载的性能。<br>
Universal-Image-Loader不仅实现了上面所说的三级缓存，而且对内存释放进行了优化。<br>
Universal-Image-Loader的开源库地址：https://github.com/nostra13/Android-Universal-Image-Loader<br>
<br>
（3）使用自定义控件ZoomImageView来扩充ImageView的功能。<br>
由于ImageView并没有能对图片使用手势控制的功能，因此需要自定义控件来实现对图片的<br>
手势缩放、缩放后对图片平移、双击缩放等功能。<br>
<br>
（3）自定义的这个ZoomImageView还是稍微有点性能问题，所以使用了更加优秀的开源库PhotoView<br>
该开源库不仅实现了多点触碰缩放、双击缩放、平滑移动的功能，而且能与Universal-Image-Loader<br>
很好地适配，与ViewPager相配合也非常完美。<br>
PhotoView的开源库地址：https://github.com/chrisbanes/PhotoView<br>
<br>
（4）为了将JSON数据转换成HashMap格式的数据，使用了阿里巴巴公司开源的库FastJson，该开源库<br>
不仅能将List、Map等复杂数据结构抽象成类的形式进行存储，而且存储与解析的速度非常快。<br>
FastJson的开源库地址：https://github.com/alibaba/fastjson<br>
<br>
（5）使用自定义控件TopBar来自行重新定义顶部动作条的功能。<br>
<br>
（6）ListView使用了convertView + ViewHolder的方法提升了性能。<br>
<br>
（7）使用SharedPreference缓存接收到的JSON字符串，实现了将数据缓存到本地的功能，即使处于<br>
离线状态也能使用本App；当手机联网时则会采用线上的数据。<br>
<br>
（8）使用蒲公英提供的SDK实现了软件的自动更新功能，一旦连接网络，就能自动检测是否有最新<br>
的版本，如果有则出现对话框供用户选择是否下载更新。用户也可以进入设置界面自行选择软件更新。<br>
<br>
<br>
####后期需要优化的地方：<br>
（1）添加用户登录界面，实现更加精准的内容推荐，自建服务器，使用大数据技术根据用户的
浏览习惯数据进行数据挖掘，推荐内容更加精准，提高用户体验。<br>
<br>
####下面是软件的截图：<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/01.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/02.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/03.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/04.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/05.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/06.jpeg)
<br>
<br>
<br>
<br>
![image](https://github.com/ZhaoYukai/ManhuaHouse/blob/master/%E7%A4%BA%E4%BE%8B%E5%9B%BE%E7%89%87/07.jpeg)
<br>
<br>
