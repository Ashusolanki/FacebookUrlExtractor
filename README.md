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
    implementation 'com.github.Ashusolanki:FacebookUrlExtractor:0.0.1'
 }
```  

## Usage

#FacebookExtractor
```

            new FacebookExtractor() 
            {
                @Override
                protected void onExtractionComplete(FacebookFile fF)
                {
                    //Extraction Complete
                }

                @Override
                protected void onExtractionFail(String Error)
                {
                    //Extraction Fail
                }
            }.Extractor(Context, videoURL);


```

