# selenium-lib

<b>fine tuned function library based on selenium webdriver api</b>

This is an example of automated tests of a web application
- by usage of junit and Excel test data by Junit 4
- by usage of Selenium 4 / ExtentReport 5
- by automation with key driven framework
- by storage of web locators in property files -> no compile needed after changes

And the best:
- flexible framework for possible usage of different or more then one java automation platform<br>
  eg Selenium & UFT Developer (in this project not yet provided)

Libs in use:
- Selenium 4
- ExtentReport 5
- org.apache.poi 3
- junit 4
- log4j2
- maven 3

<b>Run all existing junit4 tests:</b>

- open console window (windows command or linux shell)
- navigate to root directory of this project
- call: mvn test
- or call: mvn test -Dbrowser=firefox -Dmandant=ooe -DtestEnvironment=prod

If needed add additional proxy settings with your credentials:
-Dproxy=yourProxyHost -DproxyUser=yourProxyUser -DproxyPass=yourProxyPassword

There is no need to download any selenium browser driver - this is done like magic by the maven artifact io.github.bonigarcia.webdrivermanager
The selenium drivers will be loaded at runtime automatically from:
src/test/resources
If you wish to define a different directory for the download of selenium browser drivers, 
then you can set the system property 'wdm.cachePath' as maven parameter, eg:
-Dwdm.cachePath=src/test/MyDriverDir

<b>Following sites will be tested by above mvn calls</b><br>

* web application mtours<br>
    * https://demo.guru99.com/test/newtours/index.php<br>
    * http://localhost:8881/servlets/com.mercurytours.servlet.WelcomeServlet<br>
    see: https://admhelp.microfocus.com/alm/Mercury_Tours/Install_Mtous_App_Windows.htm<br><br>
  
* web application jpetstore<br>
    * https://jpetstore.aspectran.com/catalog/<br>
    * http://localhost:8080/jpetstore/actions/Catalog.action<br>
    see: https://github.com/mybatis/jpetstore-6<br>






