# Bank-Transfer

`bank-transfer` is a service that provides two apis. **firstly** Account Creation.
and **secondly** Transfer fund between two accounts. It supports concurrent request from multiple services.


#### How to run?
 
For test purpose, a main class is provided so that using command promt we can test the behavior of the system.

##### Prerequisition to run
1. JDK ( 1.8 or latest version )
2. Apache-Maven ( 3.3 or latest ) 

Please check both dependencies using below commands:
* Check Java and the output format 
```
$ java -version

java ....
Java(TM) SE Runtime Environment 18.9 (build 11.0.3+12-LTS)
Java HotSpot(TM) 64-Bit Server VM 18.9 (build 11.0.3+12-LTS, mixed mode)
```
* Check maven and output format should match
```
$ mvn -version

Apache Maven 3.3.3 (...)
Maven home: ...
Java version: ....
Java home: ...
Default locale: en_SG, platform encoding: US-ASCII
OS name: "mac os x", version: "10.13.6", arch: "x86_64", family: "mac"
```

If both presents in your machine, you are ready to run the program. Execute:
```
$ mvn clean compile exec:java 
```
After successful, execution of the above command program will start on the console.

There is one more way to build and run the program. 
Use below command to run junit tests and create jar.
```
$ mvn clean package
```
on successful execution, `bank-transfer-0.0.1.jar` will be generated under `./target` directory.
Then you can execute using java command as below:
```
java -jar target/bank-transfer-0.0.1.jar
```

#### Background:

Main problem behind this problem is concurrent and parallelism of transactions. It would be really easy when concurrency/parallelism is not required. We would like to 
solve this problem achieving both parallelism and concurrency.<br/>

**Concurrency** is when two or more tasks can start, run, and complete in overlapping time periods. It doesn't necessarily mean they'll ever both be running at the same instant. For example, multitasking on a single-core machine.

**Parallelism** is when tasks literally run at the same time, e.g., on a multicore processor.
 



