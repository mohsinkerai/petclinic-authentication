/**
 * Pitchly Webcam Interface.
 *
 * (c) Pitchly 2016.
 * @license: MIT
 *
 * @version: 0.0.1
 * @author: Blake Barrett
 */
'use strict';

window.webcam = (function () {

    var MINIMUM_RESOLUTION_WIDTH = 640;
    var MINIMUM_RESOLUTION_HEIGHT = 360;
    var MEDIUM_RESOLUTION_WIDTH = 1280;
    var MEDIUM_RESOLUTION_HEIGHT = 720;
    var MAXIMUM_RESOLUTION_WIDTH = 1920;
    var MAXIMUM_RESOLUTION_HEIGHT = 1080;
    var ASPECT_RATIO_16_9 = 1.7777777;
    var MIN_FRAME_RATE = 12;
    var MAX_FRAME_RATE = 24;

    var VIDEO_WEBM = 'video/webm';
    var WEBM_BLOB_TYPE = VIDEO_WEBM;

    var mediaRecorder;
    var options = {};
    var stream;
    var recordedBlobs = [];

    var audioSources = [];
    var videoSources = [];

    navigator.getUserMedia = (navigator.getUserMedia ||
        navigator.webkitGetUserMedia ||
        navigator.mozGetUserMedia ||
        navigator.msGetUserMedia);

    function hasRecordedVideo() {
        return recordedBlobs.length !== 0;
    }

    function getVideoBlobStream() {
        var superBuffer = new Blob(recordedBlobs, { type: WEBM_BLOB_TYPE });
        return (window.URL || window.webkitURL).createObjectURL(superBuffer);
    }

    return {
        /**
         * After calling `init()` this will be initialized as a
         * boolean; before calling `init`, it will be `undefined`.
         * Will only return true IFF both a webcam AND a microphone are found.
         * Not a function, but a public variable property.
         */
        hasWebcam: undefined,

        /**
         * After calling `init()` this will be initialized as a
         * boolean; before calling `init`, it will be `undefined`.
         * Will only return true IFF MediaRecorder can be initialized and a
         * supported codec can be selected.
         * Not a function, but a public variable property.
         */
        canRecordMedia: undefined,

        /**
         * Checks to see if there is are any webcams and microphones available.
         * If found, it will start a session with the constraints passed in.
         *
         * @returns {Promise}
         *   If the promise is successful the stream from the webcam is returned.
         *   If the promise fails, the error is returned.
         */
        initCamera: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                var initGetUserMedia = function () {
                    var audioSource = audioSources[0];
                    var videoSource = videoSources[0];

                    if (self.stream) {
                        self.stream.stop();
                    }
                    // Constraints documentation:
                    // http://w3c.github.io/mediacapture-main/#media-track-supported-constraints
                    var constraints = {
                        audio: {
                            optional: [{
                                sourceId: audioSource
                            }],
                            echoCancellation: true
                        },
                        video: {
                            optional: [{
                                sourceId: videoSource,
                            }],
                            facingMode: 'user',
                            echoCancellation: true,
                            frameRate: {
                                min: MIN_FRAME_RATE,
                                ideal: MAX_FRAME_RATE,
                                max: MAX_FRAME_RATE
                            },
                            mandatory: {
                                minAspectRatio: ASPECT_RATIO_16_9,
                                maxAspectRatio: ASPECT_RATIO_16_9
                            },
                            width: {
                                min: MINIMUM_RESOLUTION_WIDTH,
                                ideal: MEDIUM_RESOLUTION_WIDTH,
                                max: MAXIMUM_RESOLUTION_WIDTH
                            },
                            height: {
                                min: MINIMUM_RESOLUTION_HEIGHT,
                                ideal: MEDIUM_RESOLUTION_HEIGHT,
                                max: MAXIMUM_RESOLUTION_HEIGHT
                            }
                        }
                    };

                    var gotUserMedia = function (stream) {
                        self.stream = stream; // make stream available to window
                        if (window.URL) {
                            self.videoStream = window.URL.createObjectURL(self.stream);
                        } else {
                            self.videoStream = self.stream;
                        }
                        resolve(self.videoStream);
                    };

                    try {
                        // Firefox
                        navigator.mediaDevices.getUserMedia({ audio: true, video: true })
                            .then(gotUserMedia)
                            .catch(reject);
                    } catch (e) {
                        // Chrome
                        navigator.getUserMedia(constraints, gotUserMedia, reject);
                    }
                }

                var onGetDeviceSources = function (sourceInfos) {
                    for (var i = 0; i < sourceInfos.length; ++i) {
                        var sourceInfo = sourceInfos[i];
                        var kind = sourceInfo.kind;

                        if (kind.indexOf('audio') === 0) {
                            audioSources.push(sourceInfo);
                        } else if (kind.indexOf('video') === 0) {
                            videoSources.push(sourceInfo);
                        }
                    }

                    // set hasWebcam;
                    self.hasWebcam = (audioSources.length > 0 && videoSources.length > 0);
                    // if we don't have one, don't bother with it.
                    if (self.hasWebcam) {
                        initGetUserMedia();
                    } else {
                        reject('No webcams found');
                    }
                }

                if (typeof navigator.mediaDevices !== 'undefined' &&
                    typeof navigator.mediaDevices.enumerateDevices === 'function') {
                    navigator.mediaDevices.enumerateDevices().then(onGetDeviceSources).catch(reject);
                } else
                    if (typeof MediaStreamTrack !== 'undefined' &&
                        typeof MediaStreamTrack.getSources === 'function') {
                        // MediaStreamTrack.getSources is deprecated.
                        // See https://www.chromestatus.com/feature/4765305641369600 for more details.
                        MediaStreamTrack.getSources(onGetDeviceSources);
                    } else {
                        // nope. Better get Chrome.
                        alert('Unfortunately, this browser isn\'t supported.\nYou should consider downloading Google Chrome.\n\nhttps://www.google.com/chrome/');
                        reject('MediaDevices.enumerateDevices() and MediaStreamTrack.getSources() are not supported in this browser.');
                        return;
                    }
            });
        },

        /**
         * Creates a MediaRecorder session with the webcam that was initialized
         * in `initCamera`.
         *
         * @returns {Promise}
         *    If successful, the Promise returns the `MediaRecorder` object
         *      instance.
         *    Promise will call the `reject` function if no supported mimeTypes
         *      are found.
         */
        initRecorder: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                var WEBM = { mimeType: VIDEO_WEBM };
                var WEBM_VP9 = { mimeType: VIDEO_WEBM + ',codecs=vp9' };
                var WEBM_VP8 = { mimeType: VIDEO_WEBM + ',codecs=vp8' };
                var VIDEO_VP8 = 'video/vp8';

                var error = function (error) {
                    self.canRecordMedia = false;
                    reject('Cannot identify mimeTypes supported by MediaRecorder. ' + error);
                }

                // Firefox
                var mediaRecorderTryCatch = function (errorCallback) {
                    var mediaRecorder;
                    try {
                        mediaRecorder = new MediaRecorder(self.stream, WEBM);
                    } catch (e0) {
                        try {
                            mediaRecorder = new MediaRecorder(self.stream, WEBM_VP9);
                        } catch (e1) {
                            try {
                                mediaRecorder = new MediaRecorder(self.stream, VIDEO_VP8);
                            } catch (e2) {
                                errorCallback(e2);
                                return;
                            }
                        }
                    }
                    return mediaRecorder;
                }

                // Chrome
                var mediaRecorderIsTypeSupported = function (errorCallback) {
                    var options = {};
                    switch (true) {
                        case (MediaRecorder.isTypeSupported(VIDEO_WEBM)):
                            options = WEBM;
                            break;
                        case (MediaRecorder.isTypeSupported(VIDEO_WEBM + ';codecs=vp9')):
                            options = WEBM_VP9;
                            break;
                        case (MediaRecorder.isTypeSupported(VIDEO_WEBM + ';codecs=vp8')):
                            options = WEBM_VP8;
                            break;
                        case (MediaRecorder.isTypeSupported('video/vp8')):
                            options = VIDEO_VP8; // Chrome 47
                            break;
                        default:
                            errorCallback();
                            return;
                    }
                    return new MediaRecorder(self.stream, options);
                }

                if (typeof MediaRecorder.isTypeSupported === 'undefined') {
                    // Firefox
                    self.mediaRecorder = mediaRecorderTryCatch();
                } else {
                    // Chrome
                    self.mediaRecorder = mediaRecorderIsTypeSupported();
                }

                if (typeof self.mediaRecorder === 'undefined') {
                    // reject should've been called by now, but we still need
                    // to escape.
                    return;
                }

                recordedBlobs = [];
                self.canRecordMedia = true;
                self.mediaRecorder.ondataavailable = (function (event) {
                    if (event.data && event.data.size > 0) {
                        recordedBlobs.push(event.data);
                    }
                });
                resolve(self.mediaRecorder);
            });
        },

        /**
         * Starts/Stops recording the stream from the webcam.
         * Calling `record()` while a stream is already being recorded, will
         * toggle recording.
         *
         * @returns {Promise}
         *    resolve is called and returns the recorder.
         *    reject is called if the webcam is not initialized, or in the case
         *        of an unknown MediaRecorder state.
         */
        record: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                if (typeof self.mediaRecorder === 'undefined') {
                    reject('Must call initRecorder first.');
                    return;
                }
                switch (self.mediaRecorder.state) {
                    case 'inactive':
                        self.mediaRecorder.start();
                        break;
                    case 'recording':
                        self.mediaRecorder.stop();
                        break;
                    default:
                        reject(self.mediaRecorder.state);
                        return;
                }
                resolve(self.mediaRecorder);
            });
        },

        /**
         * Stops recording the stream from the webcam.
         *
         * @returns {Promise}
         *    resolve is called when the recorder is stopped.
         *    reject is called if the recorder was not recording anything, or
         *        if the mediaRecorder was never initialized.
         */
        stop: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                try {
                    self.mediaRecorder.stop();
                    resolve(self.mediaRecorder);
                } catch (error) {
                    reject(error);
                }
            });
        },

        /**
         * Downloads the last recording session.
         *
         * @returns {Promise}
         *     resolve returns the webcam object
         *     reject returns a string
         */
        download: function (fileName) {
            var self = this;
            fileName = (fileName || 'recorded') + '.webm';
            return new Promise(function (resolve, reject) {
                if (!hasRecordedVideo()) {
                    reject('Nothing recorded.');
                    return;
                }

                var videoStream = getVideoBlobStream();
                var a = document.createElement('a');
                a.style.display = 'none';
                a.href = videoStream;
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
                setTimeout(function () {
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(videoStream);
                }, 100);

                resolve(self);
            });
        },

        /**
         * Uploads the last recording session.
         * @param {Object} options. Object containing all pertinent info needed to construct the ajax request.
         * @example var example = {
         *      type: 'POST',
         *      url: 'https://example.com/upload',
         *      params: {
         *          bar: 'baz'
         *      },
         *      headers: {
         *          'Authorization': 'bearer 0123456789'
         *      },
         *      progress: function(event) {
         *          var percentComplete = event.loaded / event.total;
         *          alert(percentComplete + '% uploaded');
         *      },
         *      success: function () {
         *          alert('Yay!');
         *      },
         *      error: function() {
         *          alert('boo');
         *      }
         *   }
         *
         * @returns {Promise}
         *      resolve returns the request
         *      reject returns the error
         */
        upload: function (options) {
            var method = (options.method || 'POST');
            var url = (options.url);
            var params = (options.params || {});
            var headers = (options.headers || {});
            var progress = options.progress;
            var sucess = options.success;
            var error = options.error;
            var fileName = (options.fileName || 'recording.webm');

            return new Promise(function (resolve, reject) {
                if (!hasRecordedVideo()) {
                    reject('Nothing recorded.');
                    return;
                }
                if (typeof url === 'undefined') {
                    reject('No URL to upload provided.');
                }

                try {
                    var formData = new FormData();
                    for (var key in params) {
                        var value = params[key];
                        formData.append(key, value);
                    }

                    var videoStream = new Blob(recordedBlobs, { type: WEBM_BLOB_TYPE });
                    formData.append('file', videoStream, fileName);

                    var request = new XMLHttpRequest();
                    request.open(method, url);

                    for (var key in headers) {
                        var value = headers[key];
                        request.setRequestHeader(key, value);
                    }

                    if (typeof progress === 'function') {
                        request.addEventListener('progress', progress);
                    }
                    if (typeof success === 'function') {
                        request.addEventListener('load', success);
                    }
                    if (typeof error === 'function') {
                        request.addEventListener('error', error);
                        request.addEventListener('abort', error);
                    }

                    request.send(formData);
                    resolve(request);
                } catch (e) {
                    reject(e);
                }
            });
        },

        /**
         * Plays the recorded/captured stream from the last recording session.
         * If a `<video />` element is passed, it will have its src set to the
         * stream, and `.play()` will be called.
         *
         * @returns {Promise}
         *     resolve return the stream
         *     reject returns a string
         */
        play: function (videoElement) {
            return new Promise(function (resolve, reject) {
                if (!hasRecordedVideo()) {
                    reject('Nothing recorded.');
                    return;
                }

                var videoStream = getVideoBlobStream();
                if (videoElement) {
                    videoElement.src = videoStream;
                    videoElement.controls = true;
                    videoElement.play()
                }

                resolve(videoStream);
            });
        },

        /**
         * Constructs everything needed to start recording.
         *
         * @returns {Promise}
         *     resolve is called with the webcam object once everything is
         *          constructed. You should be ready to call `record` after
         *          calling `init` once.
         *     reject is called if an error is encountered.
         */
        init: function () {
            var self = this;
            return new Promise(function (resolve, reject) {
                try {
                    self.initCamera().then(function () {
                        self.initRecorder().then(function () {
                            resolve(self);
                        }).catch(reject);
                    }).catch(reject);
                } catch (error) {
                    reject(error);
                }
            });
        }
    }
})();

/*
// Example usage:
window.webcam.init().then(function(cam) {
    cam.record();
}).catch(function(console.log));
*/
