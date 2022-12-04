# Party Lights

## _Application which listen to "music" and flashes with colors_ 

You will not find any sound transformation or analysis inside this project.
Only thing you'll learn from this repo is how to get microphone input inside your app.

## How it works

This app listens to audio provided by microphone, that is represented by byteArray.
To not overstress watches and drain battery unnecessary, we are taking sum of absolute values from byteArray.
Then there is quick check if current sum is bigger than max value returned subtracted by some percentage.
No equation here, but if you looking for something check [FFT](https://en.wikipedia.org/wiki/Fast_Fourier_transform)

I didn't do any tests with FFT, but realtime analysis would probably be too much for a wearOS device.
However I've checked how data provided by [AudioRecord](https://developer.android.com/reference/android/media/AudioRecord) changes listening to loud party music. Mostly pop or club type.

## What's in the future

Probably nothing, because no one really downloads apps for wearOS. It was quick simple project for fun.
But if there will be 5 people beside me that will download this from Google Play, maybe I will make some feature.
And very unlikely I will think about lights controlled by AI, just maybe, let's see if anyone will find it.

# Not yet tested in real world!
## _But will update app or README after first party!_
