# selenium-lib

<b>fine tuned function library based on selenium webdriver api</b>

this is an example of automated tests of a web application
- by usage of junit and Excel test data by Junit 4
- by usage of Selenium 4 / ExtentReport 5
- by automation with key driven framework
- by storage of web locators in property files -> no compile needed after changes

and the best:
- flexible framework for possible usage of different or more then one java automation platform<br>
  eg Selenium & UFT Developer (in this project not yet provided)

libs in use:
- Selenium 4
- ExtentReport 5
- org.apache.poi 3
- junit 4
- log4j2
- maven 3

<b>run all existing junit4 tests:</b>

- open console window (windows command or linux shell)
- navigate to root directory of this project
- call: mvn test
- or call: mvn test -Dbrowser=firefox -Dmandant=ooe -DtestEnvironment=prod

<b>Following sites will be tested by above mvn calls</b><br>

* web application mtours<br>
    * https://demo.guru99.com/test/newtours/index.php<br>
    * http://localhost:8881/servlets/com.mercurytours.servlet.WelcomeServlet<br>
    see: https://admhelp.microfocus.com/alm/Mercury_Tours/Install_Mtous_App_Windows.htm<br><br>
  
* web application jpetstore<br>
    * https://jpetstore.aspectran.com/catalog/<br>
    * http://localhost:8080/jpetstore/actions/Catalog.action<br>
    see: https://github.com/mybatis/jpetstore-6<br>






