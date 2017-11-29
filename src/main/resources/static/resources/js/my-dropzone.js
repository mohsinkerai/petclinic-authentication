(function () {
  console.log('Hello World');
  var myDropzone = new Dropzone("#my-screenshot", {
    url: "/hello",
    clickable: true,
    autoProcessQueue: false,
    accept: function (file, done) {
      console.log(file);

      var reader = new FileReader();
      // Closure to capture the file information.
      reader.onload = function(e) { 
          //get the base64 url
          var base64URL = e.target.result;
          //print to console
          // console.log(base64URL);
          
          var img = document.getElementById('my-screenshot');
          img.setAttribute('src', base64URL);

          var imageBlob = document.getElementById('image-blob');
          imageBlob.value = base64URL;
      };
      // Read in the image file as a data URL.
      reader.readAsDataURL(file);

      if (file.name == "justinbieber.jpg") {
        done("Naha, you don't.");
      }
      else { done(); }
    }
  });
})();
