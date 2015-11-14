(function () {

     var SCRIPT_ID = 'BTCGridWidget'; //this has to match what is given out

     //Load attributes
     var attribs = {env: null, pl: null, style: null, ws: null};

     var errors = new Array;
     for (var key in attribs) {
          attribs[key] = attribs[key] ? attribs[key] : document.getElementById(SCRIPT_ID).getAttribute('data-' + key);
          if (!attribs[key]) errors.push('data-' + key);
     }
     if (errors.length > 0) {
          var divId = 'btc-grid-widget';
          var html = 'The following attribute(s) for the BTC Widget is(are) missing or invalid:<p/><p/>' + errors.join(', ') +
               '<p/><b>Please copy this and put it in a request for an updated widget string to embed, with the proper attributes.</b>' +
               '<p/>Thank You!  ;-)<br/>The BTC Team';
          var div = document.createElement('div');
          div.id = divId;
          var widget = document.getElementById(SCRIPT_ID);
          widget.parentNode.insertBefore(div, widget);
          document.getElementById(divId).innerHTML = html
          return;
     }

     //Load jQuery
     var script = document.createElement('SCRIPT');
     script.src = attribs.env + '/assets/js/jquery.min.js';
     script.type = 'text/javascript';
     document.getElementsByTagName("head")[0].appendChild(script);

     //load YT
     if (window.YT) window.YT = null;
     script = document.createElement("SCRIPT");
     script.src = attribs.env + '/assets/js/youtube.js';
     script.type = 'text/javascript';
     document.getElementsByTagName("head")[0].appendChild(script);

     var jqueryloadinitiated = new Date().getTime();
     var checkJQueryReady = function (callback) {
          if (window.jQuery && window.jQuery.fn.jquery >= '2.1.4') {
               var codeloaded = new Date().getTime();
               var loaddelay = ((codeloaded - jqueryloadinitiated) / 1000).toFixed(3);
               console.log("BTC Log: Time to load jQuery: " + loaddelay);
               callback(true);
          } else {
               window.setTimeout(function () {
                    checkJQueryReady(callback);
               }, 100);
          }
     };
     var ytplayerloadinitiated = new Date().getTime();
     var checkYTPlayerReady = function (callback) {
          if (window.YT && window.YT.Player) {
               var codeloaded = new Date().getTime();
               var loaddelay = ((codeloaded - ytplayerloadinitiated) / 1000).toFixed(3);
               console.log("BTC Log: Time to load YT Player: " + loaddelay);
               callback(true);
          } else {
               window.setTimeout(function () {
                    checkYTPlayerReady(callback);
               }, 100);
          }
     };

     checkJQueryReady(function (jQuery) {
          //Bootstrap the initial div into the DOM
          var $d = document;
          var OO;

          var json = {
               "grid": [
                    {
                         "playlistid": "PLLVtQiMiCJeENKOilvJvDv2xd22tTSjzO",
                         "comic": "Church Lady",
                         "joke": "Elvis Twerking",
                         "thumb": "https://i.ytimg.com/vi/1z7im3MTFuU/mqdefault.jpg",
                         "videoid": "1z7im3MTFuU",
                         "talent": "Dana Carvey"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeGWZ21zwXRPSmw2sZrUp6en",
                         "comic": "Deep Thoughts",
                         "joke": "Two Sacks",
                         "thumb": "https://i.ytimg.com/vi/7Cj7oaGMLSg/mqdefault.jpg",
                         "videoid": "7Cj7oaGMLSg",
                         "talent": "Jack Handey"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeEYYHFRMDVLmldoiegLStcy",
                         "comic": "Joe Dirt",
                         "joke": "",
                         "thumb": "https://i.ytimg.com/vi/QxQlucRPUhY/mqdefault.jpg",
                         "videoid": "QxQlucRPUhY",
                         "talent": "David Spade"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeGf0DP7UgvPpQ6zmcwXfjqz",
                         "comic": "Mall Show",
                         "joke": "",
                         "thumb": "https://i.ytimg.com/vi/SgRX3tzxtxg/mqdefault.jpg",
                         "videoid": "SgRX3tzxtxg",
                         "talent": "David Spade"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeHYIqFdQiQ2SlHUTh12io-1",
                         "comic": "Note to Self",
                         "joke": "",
                         "thumb": "https://i.ytimg.com/vi/ZiGv5ZLw1Js/mqdefault.jpg",
                         "videoid": "ZiGv5ZLw1Js",
                         "talent": "Julianne Moore"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeFo48auR_WzdQlA_76VIMLb",
                         "comic": "Sketchy Coffee",
                         "joke": "",
                         "thumb": "https://i.ytimg.com/vi/rER3K5vGwbQ/mqdefault.jpg",
                         "videoid": "rER3K5vGwbQ",
                         "talent": "Sarah Silverman"
                    },
                    {
                         "playlistid": "PLLVtQiMiCJeGH4sOLnvW_vecxT-HbcCqH",
                         "comic": "This Just In",
                         "joke": "Domestic Disturbance",
                         "thumb": "https://i.ytimg.com/vi/RTJFhWAQigw/mqdefault.jpg",
                         "videoid": "RTJFhWAQigw",
                         "talent": "Dana Carvey"
                    }
               ]
          };


          var container_div = $d.createElement('div');
          container_div.id = 'btc_container';
          var content = $d.getElementById(SCRIPT_ID);
          content.parentNode.insertBefore(container_div, content);

          //Load Stylesheets
          var loadCSS = function (href) {
               var cssLink = $("<link>");
               $("head").append(cssLink); //IE hack: append before setting href
               cssLink.attr({
                    rel: "stylesheet",
                    type: "text/css",
                    href: href
               });
          };
          //bootstrap css
          loadCSS(attribs.env + '/assets/css/style.css');
          loadCSS('http://fonts.googleapis.com/css?family=Permanent+Marker');

          var applyStyles = function () {
               if (attribs.style === 'website') {
                    $("#btc-edge-top").hide();
                    $("#btc-film-strip").hide();
                    $("#btc-main-container").css("background-color", "transparent");
                    $("#btc-footer").hide();
                    $("#btc-edge-bottom").hide();
               }
          }

          var getUrlParameter = function (sParam) {
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

          var redirectToBTCPage = function (url, comic, talent) {
               var param = url === 'home' ? '' : '?PL=' + url + '&C=' + encodeURIComponent(comic) + '&T=' + encodeURIComponent(talent) + '#btc-edge-top';
               var protocol = window.location.protocol + '//';
               var hostname = window.location.hostname;
               var port = window.location.port !== "" ? ":" + window.location.port : "";
               var pathname = window.location.pathname;
               url = protocol + hostname + port + pathname + param;
               window.location.replace(url);
          };

          var loadGridPropertiesSuccess = function (json) {
               var i = 1;
               $.each(json, function (key, val) {
                    var thumb = val.thumb
                    var comic = val.comic;
                    var joke = i <= 2 ? val.joke : ""; //Only put the joke on the first two videos
                    var talent = val.talent;
                    var playlistid = val.playlistId;
                    var img = '<img src="' + thumb + '" alt="' + comic + '"/>';
                    var slide = img +
                         "<div class='caption'>" +
                         "<div class='title'>" + comic +
                         "<div class='joke'>" + joke + "</div>" +
                         "</div>" +
                         "<div class='voice'>" + talent + "</div>" +
                         "</div>";
                    $("#vid-item" + i).html(slide);

                    var vid = "#vid-item" + i;

                    $('#btc-content').on('click', vid, function () {
                         redirectToBTCPage(playlistid, comic, talent);
                    });
                    i++;
               });
          };

          var loadGridPropertiesFailure = function (data) {
               console.log("Error loading grid", data);
          };

          var loadGridProperties = function () {
               //TODO: Replace with real call
               var gridJsonUrl = attribs.ws + "/btc-svc/ws/getGridJson";
               $.getJSON(gridJsonUrl, function (data) {
                    loadGridPropertiesSuccess($.parseJSON(data.json));
               })
                    .fail(function (data) {
                         loadGridPropertiesFailure(data);
                    });
          };

          var loadGridTemplateSuccess = function (json) {
               $('#btc_container').html(json);
               $('#pageblurb').html("New! Cartoon Shorts");
               applyStyles();
               loadGridProperties();
          };

          var loadGridTemplateFailure = function (data) {
               console.log("Error loading template", data);
          };

          var loadGridTemplate = function () {
               var gridTemplateUrl = attribs.ws + "/btc-svc/ws/getGridTemplate?callback=?";
               $.getJSON(gridTemplateUrl, function (data) {
                    loadGridTemplateSuccess(data.json);
               })
                    .fail(function (data) {
                         loadGridTemplateFailure(data);
                    });
          };

          var loadPlayer = function () {
               var player = '<video id="movie" preload controls=true autoplay poster="' + attribs.env + PL + '.png"> \
                                   <source src="' + attribs.env + PL + '.webm" type="video/webm; codecs=vp8,vorbis" /> \
                                   <source src="' + attribs.env + PL + '.ogv" type="video/ogg; codecs=theora,vorbis" /> \
                                   <source src="' + attribs.env + PL + '.mp4" /> \
                              </video>';
               $("#btc-player-container").html(player);
          };

          var loadSlide = function (idx) {
               var comic_info = $("#btc-vid-item_" + idx).data();
               $(".comic").html(comic_info.comic);
               $(".talent").html(comic_info.talent);
               $(".btc-vid-item").css("background", "#fff");
               $("#btc-vid-item_" + idx).css("background", "#66cdaa");
          };

          var buildCarousel = function () {
               var i = 0;
               $.each(json.grid, function (key, val) {
                    $("<div>", {
                         class: "btc-vid-item",
                         id: "btc-vid-item_" + i,
                         data: {comic: val.comic, talent: val.talent}
                    }).appendTo(".btc-vid-list");
                    $("<div>", {
                         class: "thumb",
                         id: "thumb_" + i
                    }).appendTo("#btc-vid-item_" + i)
                    $("<img>", {
                         src: val.thumb
                    }).appendTo("#thumb_" + i)
                    $("<div>", {
                         class: "joke",
                         text: val.joke
                    }).appendTo("#btc-vid-item_" + i)
                    i++;
               });

               $(".btc-vid-item").bind("click", function (event) {
                    var idx = this.id.split("_")[1];
                    loadSlide(idx);
                    OO.loadPlaylist({
                         list: "PLLVtQiMiCJeG_tNuuHw_3ACvXohGh-XGv",
                         listType: "playlist",
                         index: idx
                    })

               });
          };

          var onPlayerReady = function (event) {
               //event.target.playVideo();
          };

          var onPlayerStateChange = function (event) {
               //do nothing for now
          };

          var loadYTPlayer = function () {
               checkYTPlayerReady(function ($) {
                    OO = new YT.Player('btc-player-container', {
                         playerVars: {
                              listType: 'playlist',
                              list: PL,
                              controls: 1,
                              showinfo: 0,
                              modestbranding: 1,
                              rel: 0
                         },
                         events: {
                              'onReady': onPlayerReady,
                              'onStateChange': onPlayerStateChange
                         }
                    });
                    buildCarousel();
                    loadSlide(0);
               });
          };

          var loadPlayerTemplateSuccess = function (json) {
               $('#btc_container').html(json);
               $('#pagetitle #comictitle').html(C);
               $('#pagetitle #comictalent').html('with ' + T);
               applyStyles();

               $('#more-comics-button').click(function () {
                    redirectToBTCPage('home');
               });
               loadYTPlayer();
          };

          var loadPlayerTemplateFailure = function (data) {
               console.log("Error loading template", data);
          };

          var loadPlayerTemplate = function () {
               var playerTemplateUrl = attribs.ws + "/btc-svc/ws/getPlayerTemplate?callback=?";
               $.getJSON(playerTemplateUrl, function (data) {
                    loadPlayerTemplateSuccess(data.json);
               })
                    .fail(function (data) {
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
