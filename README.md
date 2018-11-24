# Pre-Downloader-Android-Master
Android Library use to download any kind of file from internet, create by Rukundo Eric Prestein.

## Add the following dependencies in your project

### For Gladle

```
allprojects {
 repositories {
   maven { url 'https://jitpack.io' }
 }
}
```

```
dependencies {
implementation 'com.github.rukundoeric:Pre-Downloader-Android-Master:1.1.1'
}
```

### For Maven
 
 ```
  <repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

 ```
  <dependency>
	    <groupId>com.github.rukundoeric</groupId>
	    <artifactId>Pre-Downloader-Android-Master</artifactId>
	    <version>1.1.1</version>
	</dependency>
```

### And use these code

  ```ruby
  PreDownloader preDownloader=new PreDownloader(this);
    preDownloader.setDownloadUrl("Download Url");
    preDownloader.setOutputFileName("output FileName");
    preDownloader.setOutputDirectory("output Directory");
    preDownloader.setCanToastMessage(true); 
    preDownloader.setConnectionFailedMessage("Connection failed");
    preDownloader.setErrorMessage("Download failed");
    preDownloader.setProgressBar(mprogressbar); 
    preDownloader.startDownload();
```


  
  
