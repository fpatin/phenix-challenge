# Challenge Phenix

To use the job, unzip the file ```phenix-challenge-0.1.zip``` located in directory
```app/default/phenix-challenge/1.0/```

After that, execute script file located in bin subdirectory 
```phenix-challenge``` for Linux platform
```phenix-challenge.bat``` for Windows platform


You have to provide following configuration parameters
* base-dir: [String] Root directory where files are located
* date-job: [String] Date use in the job

The following parameters could be overridden
* input-dir-name: [String] (default ```data```) Name of subdirectory where transactions and reference files are located. 
* output-dir-name: [String] (default ```output```) Name of subdirectory where result files will be located
* delete-working-directory-on-exit: [true|false]  (default ```true```) indicate if job need to remove working files and directory or not. 

e.g.
```
./bin/phenix-challenge -- -Dbase-dir=/root/directory/to/data/files/ -Ddate-job=20180722 -Dinput-dir-name=inputDir 
```