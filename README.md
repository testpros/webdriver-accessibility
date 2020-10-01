webdriver-accessibility
=======================

Ever wanted to do accessibility scanning in your WebDriver tests in Java? webdriver-accessibility is a Java tool that helps you run accessibility audits using [selenium webdriver][1] and [GoogleChrome accessibility-developer-tools][2]. It relies on [GoogleChrome accessibility-developer-tools][2] to run the audit. Once the audit is run, the tool returns a meaningful Java object which can be used for reporting later. The tool also takes a screenshot of your webpage and marks errors & warnings. Currently errors are bordered with red and warnings with yellow. This project is decoupled from Webdriver project in the sense that user would need to pass along a WebDriver object. 

To start using the tool, add dependency like the below (if using Maven), but any build tool can work

```
<dependency>
   <groupId>com.accessibility</groupId>
   <artifactId>webdriver-accessibility</artifactId>
   <version>1.2.0</version>
</dependency>
```

It is assumed that the user is already on the page where accessibility scan is required to be run. This is what you would do,

```java
   AccessibilityScanner scanner = new AccessibilityScanner(driver);
   Map<String, Object> audit_report = scanner.runAccessibilityAudit();
```
You could do this in your tests,

```java
if (audit_report.containsKey("error")) {
 List<Result> errors = (List<Result>) audit_report.get("error");
 assertThat("No accessibility errors expected", errors.size(),equalTo(0));
}
```
If you want specific details of the error, you could scan through the List of errors like below. Similarly you could scan all warnings for details.

```java
List<Result> errors = (List<Result>) audit_report.get("error"); 
for (Result error : errors) {
 log.info(error.getRule());//e.g. AX_TEXT_01
 log.info(error.getUrl());//e.g. [GoogleChrome accessibility-developer-tools][2] audit rules URL
 for (String element : error.getElements()) //violated elements
  log.info(element);//e.g. #myForm > P > INPUT
}
```

Interpreting audit report
===========================
Once you run ```runAccessibilityAudit()``` method it returns a ```Map<String, Object> audit_report``` and it contains following keys,
```
  /** @type {List<Result>} */
  error, //contains all errors
  /** @type {List<Result>} */
  warning, //contains all warnings
  /** @type {String} */
  plain_report, //contains plain report
  /** @type {byte[]} */
  screenshot, //contains screenshot with bordered elements (red:errors, yellow:warnings)
```
```Result``` object is made of following,
```
 /** @type {String} */
  rule, //contains specific rule information
  /** @type {List<String>} */
  elements, //contains all element locators with errors/warnings
  /** @type {String} */
  information_link, //link to [GoogleChrome accessibility-developer-tools][3] audit rules wiki for more details
```

Reporting in your Tests
=======================

webdriver-accessibility tool is agonistic any test framework. It can be used with JUNIT, TestNG, Cucumber-JVM etc.

In TestNG or JUNIT you could use it as below,

```java
@Test
public void testAccessibility() {
   AccessibilityScanner scanner = new AccessibilityScanner(driver);
   Map<String, Object> audit_report = scanner.runAccessibilityAudit();
   
   if (audit_report.containsKey("error")) {
    List<Result> errors = (List<Result>) audit_report.get("error");
    assertThat("No accessibility errors expected", errors.size(),equalTo(0));
   }
}
```

In this project, you can find two examples of how to use this library in JUnit and Cucumber-JVM under `src/test/java/com/accessibility/examples`. Tests by default run on Chrome, please make sure your Chrome version is supported by selenium used in `webdriver-accessibility`

While JUnit example is little straight forward,  Cucumber-JVM example is little more sophisticated, check under `src/test/java/com/accessibility/example` for code. Here is a sample cucumber report (test included in the project) that demonstrates how to embed details of the output of webdriver-accessibility tool in test reports.
Here you can notice that I embedded plain audit report and screenshot. In the screenshot, you can notice that input text boxes violated [missing label rule][5] and are threfore marked with red border. There are infact 24 violations of [missing label rule][5], 
in my example below however [GoogleChrome accessibility-developer-tools][2] at most provides 5 errors/warnings of each type. Therefore only 5 input boxes with this certain violation are marked with red border.
Also the small pizza image violated [missing ALT attribute rule][6] as a warning and therefore marked as yellow. 

 ![test report](/src/test/resources/report.png?raw=true)

Contributing: 
=======================
There are several ways you can contribute:
* Build and clone the project, create your own branch and submit pull request if you like to add a feature/fix bugs etc.
* Open an issue. Even if you don't know how to fix it, feature requests or found bugs are appreciated
Disclaimer: I am no accessibility expert. I am open for suggestions.

Issues
======
Please provide necessary details to reproduce the issue and open them as appropriate

[1]: https://code.google.com/p/selenium/wiki/GettingStarted "selenium webdriver"
[2]: https://github.com/GoogleChrome/accessibility-developer-tools "GoogleChrome accessibility-developer-tools"
[3]: https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules "GoogleChrome accessibility-developer-tools audit rules"
[5]: https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#-ax_text_01--controls-and-media-elements-should-have-labels "missing label rule"
[6]: https://github.com/GoogleChrome/accessibility-developer-tools/wiki/Audit-Rules#-ax_text_02--images-should-have-an-alt-attribute-unless-they-have-an-aria-role-of-presentation "missing ALT attribute rule"

