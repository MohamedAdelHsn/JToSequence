# Merge-Small-Files-into-Sequence-File-in-Hadoop
In this README , we will discuss one of the famous use case of SequenceFiles, where we will merge large number of small files into SequenceFile. We will get to this requirement mainly due to the lack efficient processing of large number of small files in hadoop or mapreduce.

## Need For Merging Small Files:

As hadoop stores all the HDFS files metadata in namenode’s main memory(which is a limited value) for fast metadata retrieval, so hadoop is suitable for storing small number of large files instead of huge number of small files. Below are the two main disadvantage of maintaining small files in hadoop.

Below is a sample calculation on namenode main memory usage.

On average each file occupies 600 bytes of space in memory. Suppose we need to store 1 billion files of each 100 KB, then we need 60 GB of main memory on namenode and 10 TB of total storage. Suppose if we merge these files into of 100 MB file each, then 60 MB of main memory will be sufficient. Maintaining 60 MB is easier when compared to 60 GB in main memory. So, we need to merge small files into large files.

One more disadvantage of maintaining small files, from Mapreduce perspective is that, processing these files will require 1 billion map tasks to process 1 billion files of 100 KB each, as each file will be processed as one separate file split in mapreduce job.  If we merge them into 100 MB file each and we have block size of 128 MB then, we might require only 1 lakh map tasks only. So, if we merge small files into large files, then mapreduce job can be completed quickly.

So, it is not optional but mandatory to convert huge number of small files into less number of large files.


### Solutions:
In order to solve both the problems mentioned above, we need a file format that can store both file name and contents in a single file and it would be a great value add if it also supports file splitting and compression to process efficiently these files in mapreduce jobs.

We will discuss mainly two possible solutions for this.

Merging Small Files into SequenceFile
Merging Small Files into Avro File
Both SequenceFile and Avro files supports splitting and compression formats. In this post, we will discuss about the first technique of merging small files into sequencefile and next post we will provide details on merging small files into avro file.
We will merge small files into sequencefile with the help of custom record reader and custom input format classes by extending InputFormat and RecordReader classes from hadoop API.

We will process each file contents as a single record and we need below two classes to process full file as a record. In this FullFileInputFormat keys are not needed and only contents are needed. So, keys are given as NullWritable and values as BytesWritable.

In order to prevent file splitting, we are overriding isSplittable() method and returning false.

And we are overriding createRecordReader() to return a custom Record Reader instance. 

![Inkedhadoop1_LI](https://user-images.githubusercontent.com/58120325/106406761-f9699f00-6442-11eb-840d-f8e1e94395b7.jpg)
