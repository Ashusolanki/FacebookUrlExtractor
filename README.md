# FacebookUrlExtractor

Facebook Extractor Download Videos
Android based Facebook url extractor
=======================================================

These are the urls to the Instagram Video files, so you can stream or download them.

* Builds: [![JitPack](https://jitpack.io/v/Ashusolanki/FacebookUrlExtractor.svg)](https://jitpack.io/#Ashusolanki/FacebookUrlExtractor)

## Gradle

To always build from the latest commit with all updates. Add the JitPack repository:

```java
repositories {
    maven { url "https://jitpack.io" }
}
```

And the dependency:

```
dependencies 
 {
    implementation 'com.github.Ashusolanki:FacebookUrlExtractor:0.0.2'
 }
```  

## Usage

#FacebookExtractor
```

      new FacebookExtractor(this,"Video Url",false)
        {
            @Override
            protected void onExtractionComplete(ArrayList<FacebookFile> facebookFiles) {
                for (FacebookFile facebookFile : facebookFiles) {
                    Log.e("TAG","---------------------------------------");
                    Log.e("TAG","facebookFile AutherName :: "+facebookFile.getAuthor());
                    Log.e("TAG","facebookFile FileName :: "+facebookFile.getFilename());
                    Log.e("TAG","facebookFile Ext :: "+facebookFile.getExt());
                    Log.e("TAG","facebookFile Url :: "+facebookFile.getUrl());
                    Log.e("TAG","facebookFile Quality :: "+facebookFile.getQuality());
                    Log.e("TAG","---------------------------------------");
                }
            }

            @Override
            protected void onExtractionFail(Exception error) {
                Log.e("Error","Error :: "+error.getMessage());
                error.printStackTrace();
            }
        };

```

#Facebook File
```
    getQuality();
    getUrl();
    getExt();
    getFilename();
    getAuthor();

```

