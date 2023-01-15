# selenium-lib

<b>fine tuned function library based on selenium webdriver api</b>

this is an example of:
- automated test of a web application (MercuryTours)
  either from local web app<br>
  see: https://admhelp.microfocus.com/alm/Mercury_Tours/Install_Mtous_App_Windows.htm<br>
  or from https://demo.guru99.com/test/newtours/index.php
- usage of junit and Excel test data by Junit 4
- usage of Selenium 4 / ExtentReport 5
- automation by key driven framework
- storage of web locators in property files -> no compile needed after changes

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
