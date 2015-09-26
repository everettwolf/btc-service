console.clear();
(function() {
     var script;

     //Load jQuery
     script = document.createElement('SCRIPT');
     script.src = '//code.jquery.com/jquery-1.10.2.js';
     script.type = 'text/javascript';
     document.getElementsByTagName("head")[0].appendChild(script);

     var checkReady = function(callback) {
          if (window.jQuery && window.jQuery.fn.jquery >= '1.10.2') {
               callback(true);
          } else {
               console.log("polling");
               window.setTimeout(function() {
                    checkReady(callback);
               }, 100);
          }
     };

     checkReady(function($) {
          console.log("ready");

          //Bootstrap the initial div into the DOM
          var $d = document;
          var $j = jQuery.noConflict();
          var container_div = $d.createElement('div');
          container_div.id = 'btc_container';
          var content = $d.getElementById("BTC");
          content.parentNode.insertBefore(container_div, content);

          //Load Stylesheets
          var loadCSS = function(href) {
               var cssLink = $j("<link>");
               $j("head").append(cssLink); //IE hack: append before setting href
               cssLink.attr({
                    rel: "stylesheet",
                    type: "text/css",
                    href: href
               });
          };
          //bootstrap css
          loadCSS('//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css');
          loadCSS('/assets/css/style.css');



          var loadGridPropertiesSuccess = function(json) {
               console.log("ok");
               var i = 1;
               $j.each(json, function(key, val) {
                    console.log(val);
                    var img = '<img src="' + val.thumb + '" alt="Beyond the Comics"/>';
                    var title = val.comic;
                    var joke = "";
                    var voice = val.talent;
                    var slide = img +
                         "<div class='caption' >" +
                         "<div class='title'>" + title +
                         "<div class='joke'>" + joke + "</div>" +
                         "</div>" +
                         "<div class='voice'>" + voice + "</div>" +
                         "<div id='playlist1' class='playlist'>2c0aa6d110ca49738916789459b4bb53</div>" +
                         "</div>";
                    $j("#vid-item" + i).html(slide);
                    i++;
               });

               $j('#test').click(function() {
                    alert("clicked");
               });
          };
          var loadGridPropertiesFailure = function(data) {
               console.log("Error loading grid", data);
          };
          var loadGridProperties = function() {
               var gridJsonUrl = "http://localhost:8001/btc-svc/ws/getGridJson?callback=?";
               $j.getJSON(gridJsonUrl, function(data) {
                         loadGridPropertiesSuccess($j.parseJSON(data.json));
                    })
                    .fail(function(data) {
                         loadGridPropertiesFailure(data);
                    });
          };

          var loadGridTemplateSuccess = function(json) {
               $j('#btc_container').html(json);
               loadGridProperties();
          };
          var loadGridTemplateFailure = function(data) {
               console.log("Error loading template", data);
          };
          var loadGridTemplate = function() {
               var gridTemplateUrl = "http://localhost:8001/btc-svc/ws/getGridTemplate?callback=?";
               $j.getJSON(gridTemplateUrl, function(data) {
                         loadGridTemplateSuccess(data.json);
                    })
                    .fail(function(data) {
                         loadGridTemplateFailure(data);
                    });
          };
          var loadPlayer = function() {
               var playlist_id = 'http://dev.beyondthecomics.com/videos/test';
               var player = '<video id="movie" preload controls autoplay> \
                                   <source src="' + playlist_id + '.webm" type="video/webm; codecs=vp8,vorbis" /> \
                                   <source src="' + playlist_id + '.ogv" type="video/ogg; codecs=theora,vorbis" /> \
                                   <source src="' + playlist_id + '.mp4" /> \
                              </video>';
               $j("#btc-playerContainer").html(player);
          };

          loadGridTemplate();
     });
})();
