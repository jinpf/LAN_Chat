##LAN_Chat

GUI LAN Chat software written in Java, because it`s written with Java, it can run on any platforms which support JVM.

###First Edition

In `src/firstediton` ,there\`s the first edition.
In this edition ,you can communicate with others use this simple but ugly tool...First you must listen to a port by click the "监听" button ,then when someone wants to connect and chat with you ,you just need to reply messages with click "回复" button  to reply messages .Also ,you can fill in the text with IP and Port that someone else is listening,then click "连接" to connect them. Then you can text them messages and click "发送" to send it out.

**In fact it can be split into one server and one client** ,but i am  lazzy to write the GUI ,so i find a way to put it together ，but to be honst ,it\`s really ugly .SO ,i got to decide to write a better one ! So ,that makes for the next edition.

**Good for study ,but awful for real use.**

*pre-view:* **(go to https://github.com/jinpf/LAN_Chat/releases download and try it)**
* ![image](https://f.cloud.github.com/assets/5752293/2527131/6bc95188-b501-11e3-88b7-a806b5167f29.png)


###Second Edition

In `src/secondedition` ,thers\`s the second edition.
In this edition ,you can commuicate and share files with others in the same LAN .This edition is better than ever ,it\`s pretty friendly ,you don\`t need to get the f**king IP and Port ,software itself can help you discover who is online .You just need to write down you name and then click the "上线" button to go online ,then find one on the online list ,double click ,and then begin your talk .When transfer files you will find how friendly it is!

**Pretty easy to use ,but difficult to write...**

**Attention: For testing it ,don\`t be afraid to run it on the same PC ,but one PC can only run at most 5 apps .If you want to run more ,please change the source code in `MainFrame.java` and ` BrocastThread.java` : `for (int i=3000;i<3005;i++)` to `for (int i=3000;i<3000+n;i++)` ,`n` stands for how many apps you want to run.**

*pre-view:* **(go to https://github.com/jinpf/LAN_Chat/releases download and try it)**
* ![qq1](https://cloud.githubusercontent.com/assets/5752293/2569737/89f82498-b8f0-11e3-9aea-89e2c2d67d30.png)
* ![qq2](https://cloud.githubusercontent.com/assets/5752293/2569739/8db32268-b8f0-11e3-8989-a0178aeec3b3.png)
* ![image](https://cloud.githubusercontent.com/assets/5752293/2588467/ebeb8a50-ba3a-11e3-9647-e2ce5b3d841d.png)
