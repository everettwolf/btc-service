(function() {

    var SCRIPT_ID = 'BTCGridWidget'; //this has to match what is given out

    //Load attributes
    var attribs = {env:null,pl:null,style:null,ws:null};

    //Attribute overrides
    if(!attribs.pl) attribs.pl = "12345";
    if(!attribs.style) attribs.style = 'default';

    var errors = new Array;
    for(var key in attribs){
          attribs[key] = attribs[key] ? attribs[key] : document.getElementById(SCRIPT_ID).getAttribute('data-' + key);
          if(!attribs[key]) errors.push('data-' + key);
    }
    if(errors.length > 0) {
          var divId = 'btc-grid-widget';
          var html = 'The following attribute(s) for the BTC Widget is(are) missing or invalid:<p/><p/>' + errors.join(', ') +
                         '<p/><b>Please copy this and put it in a request for an updated widget string to embed, with the proper attributes.</b>' +
                         '<p/>Thank You!  ;-)<br/>The BTC Team';
          var div = document.createElement('div');
          div.id=divId;
          var widget = document.getElementById(SCRIPT_ID);
          widget.parentNode.insertBefore(div,widget);
          document.getElementById(divId).innerHTML = html;
          return;
    }

     //Load jQuery
     var script = document.createElement('SCRIPT');
     script.src = attribs.env + '/assets/js/jquery.min.js';
     script.type = 'text/javascript';
     document.getElementsByTagName("head")[0].appendChild(script);

     var checkReady = function(callback) {
          if (window.jQuery && window.jQuery.fn.jquery >= '2.1.4') {
               callback(true);
          } else {
               window.setTimeout(function() {
                    checkReady(callback);
               }, 100);
          }
     };

     checkReady(function($) {
          //Bootstrap the initial div into the DOM
          var $d = document;
          var $j = jQuery.noConflict();
          var container_div = $d.createElement('div');
          container_div.id = 'btc_container';
          var content = $d.getElementById(SCRIPT_ID);
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
          loadCSS(attribs.env + '/assets/css/style.css');
          loadCSS('http://fonts.googleapis.com/css?family=Permanent+Marker');

          var getUrlParameter = function(sParam) {
               var sPageURL = window.location.search.substring(1);
               var sURLVariables = sPageURL.split('&');
               for (var i = 0; i < sURLVariables.length; i++) {
                    var sParameterName = sURLVariables[i].split('=');
                    if (sParameterName[0] == sParam) {
                         return decodeURIComponent(sParameterName[1]);
                    }
               }
               return '';
          };

          var redirectToBTCPage = function(url,comic,talent) {
               var param = url === 'home' ? '' : '?PL=' + url + '&C=' + encodeURIComponent(comic) + '&T=' + encodeURIComponent(talent) + '#btc-edge-top';
               var protocol = window.location.protocol + '//';
               var hostname = window.location.hostname;
               var port = window.location.port !== "" ? ":" + window.location.port : "";
               var pathname = window.location.pathname;
               url = protocol + hostname + port + pathname + param;
               window.location.replace(url);
          };

          var loadGridPropertiesSuccess = function(json) {
               var i = 1;
               $j.each(json, function(key, val) {
                    var img = '<img src="' + attribs.env + '/' + val.thumb + '.png" alt="' + val.comic + '"/>';
                    var joke = "";
                    var slide = img +
                         "<div class='caption'>" +
                         "<div class='title'>" + val.comic +
                         "<div class='joke'>" + joke + "</div>" +
                         "</div>" +
                         "<div class='voice'>" + val.talent + "</div>" +
                         "</div>";
                    $j("#vid-item" + i).html(slide);

                    var vid = "#vid-item" + i;

                    $j('#btc-content').on('click', vid, function() {
                         redirectToBTCPage(val.embed_code,val.comic,val.talent);
                    });
                    i++;
               });
          };
          var loadGridPropertiesFailure = function(data) {
               console.log("Error loading grid", data);
          };
          var loadGridProperties = function() {
               var gridJsonUrl = attribs.ws + "/btc-svc/ws/getGridJson?callback=?";
               $j.getJSON(gridJsonUrl, function(data) {
                         loadGridPropertiesSuccess($j.parseJSON(data.json));
                    })
                    .fail(function(data) {
                         loadGridPropertiesFailure(data);
                    });
          };

          var loadGridTemplateSuccess = function(json) {
               $j('#btc_container').html(json);
               $j('#pageblurb').html("New! Cartoon Shorts")
               loadGridProperties();
          };
          var loadGridTemplateFailure = function(data) {
               console.log("Error loading template", data);
          };
          var loadGridTemplate = function() {
               var gridTemplateUrl = attribs.ws + "/btc-svc/ws/getGridTemplate?callback=?";
               $j.getJSON(gridTemplateUrl, function(data) {
                         loadGridTemplateSuccess(data.json);
                    })
                    .fail(function(data) {
                         loadGridTemplateFailure(data);
                    });
          };
          var loadPlayer = function() {
               var player = '<video id="movie" preload controls=true autoplay poster="' + attribs.env + PL + '.png"> \
                                   <source src="' + attribs.env + PL + '.webm" type="video/webm; codecs=vp8,vorbis" /> \
                                   <source src="' + attribs.env + PL + '.ogv" type="video/ogg; codecs=theora,vorbis" /> \
                                   <source src="' + attribs.env + PL + '.mp4" /> \
                              </video>';
               $j("#btc-playerContainer").html(player);
          };
          var loadPlayerTemplateSuccess = function(json) {
               $j('#btc_container').html(json);
               $j('#pagetitle #comictitle').html(C);
               $j('#pagetitle #comictalent').html('with ' + T);

               $j('#moreComicsButton').click(function() {
                    redirectToBTCPage('home');
               });
               loadPlayer();
          };
          var loadPlayerTemplateFailure = function(data) {
               console.log("Error loading template", data);
          };
          var loadPlayerTemplate = function() {
               var playerTemplateUrl = attribs.ws + "/btc-svc/ws/getPlayerTemplate?callback=?";
               $j.getJSON(playerTemplateUrl, function(data) {
                         loadPlayerTemplateSuccess(data.json);
                    })
                    .fail(function(data) {
                         loadPlayerTemplateFailure(data);
                    });
          };
          var PL = getUrlParameter('PL');
          var C = getUrlParameter('C');
          var T = getUrlParameter('T');
          //Remote Calls
          if (PL === '') {
               loadGridTemplate();
          } else {
               loadPlayerTemplate();
          }
     });
})();
