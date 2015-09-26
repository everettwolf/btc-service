(function () {
    //Constants
    //if (window.location.hostname === 'dev.beyondthecomics.com') {
        console.clear();
        console.log("BTC Log: Cleared the console in Development environment");
    //}
    var SCRIPT_ID = 'BTCGridWidget';
    var attribs = {env:null,pl:null,style:null,refresh:null};
    for(var key in attribs){
    	attribs[key] = document.getElementById(SCRIPT_ID).getAttribute("data-" + key)
    	console.log(attribs[key]);
    }
    /*** OVERRIDES ***
	There has been code distributed, so we need to override before we validate
	*****************/
	if (!attribs.style) attribs.style = 'default';
	if (!attribs.refresh) attribs.refresh = 'false';
	if (!attribs.env) attribs.env = 'dev.beyondthecomics.com';
	//McClatchy
	if (!attribs.pl) attribs.pl = '5e543a8d321c414a9743a3103c209d1f';

	if (window.location.hostname === 'www.montereyherald.com' || window.location.hostname === 'montereyherald.com') {
		console.log("BTC Log: Monterey Herald");
		attribs.env = 'dev.beyondthecomics.com';
		attribs.pl = 'dfe2082b02a0454b9d84a4f9d2d80375';
    }

    var msg = validateWidget(attribs);
    if (msg !== '') {
        var h = 'The following attribute(s) for the BTC Widget is missing or invalid:<br>';
        h += msg + '<br>';
        h += '<b>Please request an updated widget string to embed, with the proper attributes.</b>';
        btc_div = document.createElement('div');
        btc_div.id = 'btc-grid-widget';

        var widget = document.getElementById(SCRIPT_ID);
        widget.parentNode.insertBefore(btc_div, widget);
        document.getElementById("btc-grid-widget").innerHTML = h;
        return;
    }
    function validateWidget(attribs) {
        var msg = new Array;
        for (var key in attribs) {
            var val = attribs[key];
            if (!val) msg.push('data-' + key);
        }
        return msg.join(',');
    }
    /*** manipulate widget attributes for code  ***/
    //attribs.env = '//' + attribs.env;
    attribs.refresh = attribs.refresh === "true" ? true : false;
    var TITLE = 'New! Cartoon Shorts';

    if(!attribs.pl) attribs.pl = '5e543a8d321c414a9743a3103c209d1f';
     //Load Scripts
    var script;

    // Load jQuery
    script = document.createElement('SCRIPT');
    script.src = '//code.jquery.com/jquery-1.10.2.js';
    script.type = 'text/javascript';
    document.getElementsByTagName("head")[0].appendChild(script);

console.log("attribs.env", attribs.env);
    //Load Slider
    script = document.createElement('SCRIPT');
    script.src = attribs.env + '/js/jssor.slider.mini.js';
    script.type = 'text/javascript';
    document.getElementsByTagName("head")[0].appendChild(script);

    //Load JYPlayer
    script = document.createElement('SCRIPT');
    script.src = '//player.ooyala.com/v3/3a7ee37efcb84a67912ff58f75f2600d?namespace=JYPlayer';
    script.type = 'text/javascript';
    document.getElementsByTagName("head")[0].appendChild(script);

    // Poll for jQuery and JYPlayer to come into existence
    // We need JYPlayer on load because of apple products
    var checkReady = function (callback) {
        if (window.JYPlayer && window.jQuery && window.jQuery.fn.jquery === '1.10.2' && window.$JssorArrowNavigator$ && window.$JssorBulletNavigator$) {
            callback(window.JYPlayer && window.jQuery && window.jQuery.fn.jquery === '1.10.2' && window.$JssorArrowNavigator$ && window.$JssorBulletNavigator$);
        } else {
            window.setTimeout(function () {
                checkReady(callback);
            }, 100);
        }
    };

    // Start polling...
    checkReady(function ($) {

        //jQuery is loaded. Attempt to isolate the jQuery code for BTC
        var $j = jQuery.noConflict();
        var $d = document;
        var OO;
        console.log("BTC Log: You are running jQuery version: " + $j.fn.jquery);

        var DFPScript = "<script type='text/javascript'> \
							var gptadslots=[]; \
							var googletag = googletag || {}; \
							googletag.cmd = googletag.cmd || []; \
							(function() { \
								var gads = document.createElement('script'); \
								gads.async = true; \
								gads.type = 'text/javascript'; \
								var useSSL = 'https:' == document.location.protocol; \
								gads.src = (useSSL ? 'https:' : 'http:') + \
								'//www.googletagservices.com/tag/js/gpt.js'; \
								var node = document.getElementsByTagName('script')[0]; \
								node.parentNode.insertBefore(gads, node); \
							})(); \
							</script> \
							<script type='text/javascript'> \
								googletag.cmd.push(function() { \
								gptadslots[1] = googletag.defineSlot('/36913802/gridcenter', [180, 150], 'div-gpt-ad-1428868950414-0').addService(googletag.pubads()); \
								googletag.pubads().enableSingleRequest(); \
								googletag.pubads().enableAsyncRendering(); \
								googletag.enableServices(); \
							}); \
							</script>";
		//$j('head').append(DFPScript);

								//gptadslots[1] = googletag.defineSlot('/8013/montereyherald.com', [[300,250]],'div-gpt-ad-862669011181153353-1').setTargeting('kv',['Monterey']).addService(googletag.pubads()); \

		var DFPAdCodeSlot1 = "";
		/*<div id='div-gpt-ad-1428868950414-0' style='height:150px; width:180px;'> \
				<script type='text/javascript'> \
				googletag.cmd.push(function() { \
					googletag.defineSlot('/36913802/gridcenter', [180, 150], 'div-gpt-ad-1428868950414-0').addService(googletag.pubads()); \
					googletag.display('div-gpt-ad-1428868950414-0'); \
				}); \
				</script> \
				</div>";*/
		/*var DFPAdCodeSlot2 = "<div id='div-gpt-ad-862669011181153353-1' style='height:300px; width:250px;'> \
				<script type='text/javascript'> \
				googletag.cmd.push(function() { googletag.display('div-gpt-ad-862669011181153353-1'); }); \
				</script> \
				</div>";*/

		//Analytics
		(function (i, s, o, g, r, a, m) {
			i.GoogleAnalyticsObject = r;
			i[r] = i[r] || function () {
				(i[r].q = i[r].q || []).push(arguments);
			}, i[r].l = 1 * new Date();
			a = s.createElement(o),
			m = s.getElementsByTagName(o)[0];
			a.async = 1;
			a.src = g;
			m.parentNode.insertBefore(a, m);
		})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

		ga('create', 'UA-50041591-1', 'auto');

		var btc_slider;
		var kfad = "<script async src='//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js'></script> \
					<ins class='adsbygoogle' \
						 style='display:inline-block;width:300px;height:249px' \
						 data-ad-client='ca-pub-5403475582289341' \
						 data-ad-slot='7982932511'></ins> \
					<script> \
					(adsbygoogle = window.adsbygoogle || []).push({}); \
					</script>";
		//kfad = "<img src='//dev.beyondthecomics.com/img/kfad.png'/>";

		var gridad =  "<script async src='//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js'></script> \
								<!-- Grid Ad --> \
								<ins class='adsbygoogle' \
									 style='display:inline-block;width:180px;height:150px' \
									 data-ad-client='ca-pub-5403475582289341' \
									 data-ad-slot='9264644113'> \
								</ins> \
					<script>(adsbygoogle = window.adsbygoogle || []).push({});</script>";

		var playerad = "<script async src='//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js'></script> \
       						<ins class='adsbygoogle' \
       							style='display:inline-block;width:336px;height:280px' \
       							data-ad-client='ca-pub-5403475582289341' \
       							data-ad-slot='8968436119'></ins> \
       					<script>(adsbygoogle = window.adsbygoogle || []).push({});</script>";
    	//Load Stylesheets
        loadCSS = function (href) {
            var cssLink = $j("<link rel='stylesheet' type='text/css' href='" + href + "'>");
            $j("head").append(cssLink);
        };
        loadCSS('http://fonts.googleapis.com/css?family=Permanent+Marker');
        if(attribs.style === 'king-featuress'){
	        loadCSS(attribs.env + '/css/mediakingfeatures.css');
        }else{
        	loadCSS(attribs.env + '/css/mediadefault.css');
        }
        loadCSS(attribs.env + '/css/BTCGridStyle.css');

        //Load Additional Javascript
        loadJS = function(src) {
     		var jsLink = $j("<script type='text/javascript' src='"+src+"'>");
     		$j("head").append(jsLink);
		 };
        loadJS(attribs.env + '/js/respond.js'); //Support for @media in IE
        //see if the source is Marketing, Facebook or Twitter
        var pl = "";
        var src = "";
        var param = "";
        if(getUrlParameter('UTM') !== "undefined") {
        	pl = getUrlParameter('UTM');
        	src = 'jg';
        	param = 'UTM';
        	attribs.pl = 'b7e2c9ceae9b4867bc5d9e7a32657b48';
        }else if(getUrlParameter('FB') !== "undefined"){
        	pl = getUrlParameter('FB');
        	src = 'fb';
        	param = 'FB';
        }else if(getUrlParameter('TTR') !== "undefined"){
        	pl = getUrlParameter('TTR');
        	src = 'ttr';
        	param = 'TTR'
        }else if(getUrlParameter('PL') !== "undefined"){
        	pl = getUrlParameter('PL');
        	src = 'self';
        	param = 'PL';
        }
        pl = pl !== "" ? pl : "1";
        console.log("playlist", pl);
        //Load intitial HTML
        var html = "<div id='btc-main-container'><a href='#btc-edge-top'></a> \
						<div id='btc-edge-top'></div> \
						<div id='btc-film-strip'> \
							<div class='dot dot-top'></div> \
							<div class='dot dot-bottom'></div> \
						</div> \
						<div id='btc-main'> \
							<div id='btc-top-header'>  \
							<a href='http://beyondthecomics.com'> \
								<div id='btc-logo'> \
									<div id='logo'><img src='" + attribs.env + "/img/rays_and_logo.png' alt='logo' /></div> \
								</div>\
								</a> \
								<div id='pagetitle'> \
									<div id='pageblurb'>" + TITLE + "</div> \
									<div id='playerinfo'> \
										<div id='comictitle'></div> \
										<div id='comictalent'></div> \
									</div> \
								</div> \
							</div> \
							<div id='btc-content'></div> \
							<div id='btc-player'> \
       							<div id='btc-playerContainer'></div> \
    							<div id='btc-playerContainerBottom'><a href='#btc-edge-top' id='moreComicsButton'></a></div> \
       							<div id='btc-playerad'></div> \
							</div> \
							<div id='btc-archive'> \
								<div id='btc-archive-container'> \
								<div id='btc-navigation'> \
									<div id='btc-more-comics'>More Beyond the Comics Videos</div> \
									<div id='btc-navigate-text'>Scroll left or right &ndash; or drag &ndash; to see more choices</div> \
									<div id='btc-navigate-arrows'>\
										<div u='arrowleft' class='jssora03l' style='width: 26px; height: 26px; top: 0px; right: 26px;'></div> \
										<div u='arrowright' class='jssora03r' style='width: 26px; height: 26px; top: 0px; right: 0px'></div> \
									</div> \
								</div> \
									<div u='loading' style='position: absolute; top: 0px; left: 0px;'> \
										<div style='filter: alpha(opacity=70); opacity:0.7; position: absolute; display: block; background-color: #000; top: 0px; left: 0px;width: 100%;height:100%;'></div> \
										<div style='position: absolute; display: block; background: url(" + attribs.env + "/img/loading.gif) no-repeat center center; top: 0px; left: 0px;width: 100%;height:100%;'></div> \
									</div> \
									<div id='btc-slides' u='slides'> \
									</div> \
									<div u='navigator' class='jssorb03' style='position: absolute; bottom: 4px; right: 6px;'> \
										<div u='prototype' style='position: absolute; width: 21px; height: 21px; text-align:center; line-height:21px; color:white; font-size:12px;'> \
											<div u='numbertemplate'></div> \
										</div> \
									</div> \
								</div> \
							</div> \
							<div id='btc-footer'> \
									<div id='footer-links'><a href='/about.php'>About B.T.C.</a><a href='/contact.php' class='last'>Contact Us</a></div> \
									<div id='copyright'>copyright &copy; 2014 Beyond the Comics</div> \
							</div> \
						</div> \
						<div id='btc-edge-bottom'></div> \
					</div>";

        var btc_div = $d.createElement('div');
        btc_div.id = 'btc-grid-widget';

        var grid = $d.getElementById(SCRIPT_ID);
        grid.parentNode.insertBefore(btc_div, grid);

        $j("#btc-grid-widget").css('width', '655px');
        $j("#btc-grid-widget").html(html);
        console.log("BTC Log: Loaded default HTML");

        if(attribs.style === 'website'){
			$j("#btc-edge-top").hide();
			$j("#btc-film-strip").hide();
			$j("#btc-main-container").css("background-color", "transparent");
			$j("#btc-footer").hide();
			$j("#btc-edge-bottom").hide();
		}
		if(attribs.style === 'king-features'){
			$j("#btc-top-header").hide();
		}

		//responsive code begin

		var slide_width = ($j("#btc-grid-widget").width() - 30)/4;
		var slide_height = slide_width * 0.5617;

		function scaleBTCElements() {
		//alert("main container width: " + $j('#btc-main-container').width());
		//alert("main width: " + $j('#btc-main').width());
		//alert("player width: " + $j('#btc-player').width());
			var bodyWidth = $j('#btc-archive').width();
			if(btc_slider){
				if (bodyWidth) btc_slider.$ScaleWidth(Math.min(bodyWidth, 640));
				else window.setTimeout(scaleBTCElements, 30);
			}
			$j('#btc-playerContainer').width($j('#btc-player').width()).height(($j('#btc-player').width()*0.5645)+150);
		//alert("player container width: " + $j('#btc-playerContainer').width());
		}
		scaleBTCElements();

		$j(window).bind("load", scaleBTCElements);
		$j(window).bind("resize", scaleBTCElements);
		$j(window).bind("orientationchange", scaleBTCElements);

         var xml = {"NpYWNodzpiEoSWi3paQe2fkhXz9Q6B-i":{"comic":"Joe Dirt","title":"New Sayings!","talent":"David Spade","thumb":"http:\/\/cf.c.ooyala.com\/NpYWNodzpiEoSWi3paQe2fkhXz9Q6B-i\/3Gduepif0T1UGY8H4xMDoxOjA4MTsiGN","embed_code":"/assets/videos/test","playlist_id":"/assets/videos/test","updated_at":"2015-09-11T23:02:22Z"}};

        $j('#btc-archive').hide();
       writeDivs(xml);

        function loadSlider(){
			var btc_slider_options = {
			   $AutoPlay: false, //[Optional] Whether to auto play, to enable slideshow, this option must be set to true, default value is false
			   $AutoPlaySteps: 4, //[Optional] Steps to go for each navigation request (this options applys only when slideshow disabled), the default value is 1
			   $AutoPlayInterval: 4000, //[Optional] Interval (in milliseconds) to go for next slide since the previous stopped if the slider is auto playing, default value is 3000
			   $PauseOnHover: 1, //[Optional] Whether to pause when mouse over if a slider is auto playing, 0 no pause, 1 pause for desktop, 2 pause for touch device, 3 pause for desktop and touch device, 4 freeze for desktop, 8 freeze for touch device, 12 freeze for desktop and touch device, default value is 1

			   $ArrowKeyNavigation: true, //[Optional] Allows keyboard (arrow key) navigation or not, default value is false
			   $SlideDuration: 160, //[Optional] Specifies default duration (swipe) for slide in milliseconds, default value is 500
			   $MinDragOffsetToSlide: 20, //[Optional] Minimum drag offset to trigger slide , default value is 20
			   $SlideWidth: 152.5, //[Optional] Width of every slide in pixels, default value is width of 'slides' container
			   //$SlideHeight: 78.64, //[Optional] Height of every slide in pixels, default value is height of 'slides' container
			   $SlideSpacing: 10, //[Optional] Space between each slide in pixels, default value is 0
			   $DisplayPieces: 4, //[Optional] Number of pieces to display (the slideshow would be disabled if the value is set to greater than 1), the default value is 1
			   $ParkingPosition: 0, //[Optional] The offset position to park slide (this options applys only when slideshow disabled), default value is 0.
			   $UISearchMode: 1, //[Optional] The way (0 parellel, 1 recursive, default value is 1) to search UI components (slides container, loading screen, navigator container, arrow navigator container, thumbnail navigator container etc).
			   $PlayOrientation: 1, //[Optional] Orientation to play slide (for auto play, navigation), 1 horizental, 2 vertical, 5 horizental reverse, 6 vertical reverse, default value is 1
			   $DragOrientation: 1, //[Optional] Orientation to drag slide, 0 no drag, 1 horizental, 2 vertical, 3 either, default value is 1 (Note that the $DragOrientation should be the same as $PlayOrientation when $DisplayPieces is greater than 1, or parking position is not 0)
			   $LazyLoading: 1,

			   $BulletNavigatorOptions: { //[Optional] Options to specify and enable navigator or not
				   $Class: $JssorBulletNavigator$, //[Required] Class to create navigator instance
				   $ChanceToShow: 0, //[Required] 0 Never, 1 Mouse Over, 2 Always
				   $AutoCenter: 0, //[Optional] Auto center navigator in parent container, 0 None, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
				   $Steps: 1, //[Optional] Steps to go for each navigation request, default value is 1
				   $Lanes: 1, //[Optional] Specify lanes to arrange items, default value is 1
				   $SpacingX: 0, //[Optional] Horizontal space between each item in pixel, default value is 0
				   $SpacingY: 0, //[Optional] Vertical space between each item in pixel, default value is 0
				   $Orientation: 1 //[Optional] The orientation of the navigator, 1 horizontal, 2 vertical, default value is 1
			   },

			   $ArrowNavigatorOptions: {
				   $Class: $JssorArrowNavigator$, //[Requried] Class to create arrow navigator instance
				   $ChanceToShow: 2, //[Required] 0 Never, 1 Mouse Over, 2 Always
				   $AutoCenter: 0, //[Optional] Auto center navigator in parent container, 0 None, 1 Horizontal, 2 Vertical, 3 Both, default value is 0
				   $Steps: 4 //[Optional] Steps to go for each navigation request, default value is 1
			   }

		   };

		   btc_slider = new $JssorSlider$("btc-archive-container", btc_slider_options);
		   scaleBTCElements();
       }

        function getAssetInfo() {
            var AssetInfo = {};
            console.log("BTC Log: Getting Asset Info for " + attribs.pl);

            //var url = 'http://localhost:8001/btc-svc/ws/getGridInfo';
            var url = 'http://beyondthecomics.com/php/BTC_Grid.php?pl=' + attribs.pl + '&op=assets';
            console.log(url);
            //return;
            $j.ajax({
                url: url,
                dataType: 'json',
                cross_domain: true,
                success: function (xml) {
                console.log("xml",xml);
                    console.log("BTC Log: Retrieved feed for asset info");
                    writeDivs(xml);
                },
                error: function (xml) {
                    console.log("BTC Log: Can't connect to video server to retrieve asset info");
                }
            });
        }
        function getArchiveInfo() {
            console.log("BTC Log: Getting Archive Info");
            var url = 'http://beyondthecomics.com/php/BTC_Grid.php?pl=' + attribs.pl + '&op=archive';
            $j.ajax({
                url: url,
                dataType: 'json',
                cross_domain: true,
                success: function (xml) {
                    console.log("BTC Log: Retrieved feed for archive info");
                    if(xml.length === 0){
                    	$j('#btc-archive').hide();
                    }else{
						$j.each(xml, function (key, val) {
							$j('#btc-slides').append("<div><img u='image' class='btc-thumb' comic='" + (val.comic).replace(/'/g, "%27") + "'  title='" + val.title + "' talent='" + val.talent + "' playlist_id='" + val.playlist_id + "' src='" + val.thumb + "' /></div>");
						});
						loadSlider();
						$j(".btc-thumb").click(function(){
							var comic =  decodeURIComponent(this.getAttribute("comic")).replace(/%27/g, "'");
							var title =  decodeURIComponent(this.getAttribute("title")).replace(/%27/g, "'");
							var talent =  decodeURIComponent(this.getAttribute("talent")).replace(/%27/g, "'");
							var playlist_id = this.getAttribute("playlist_id");
							if (OO) {
								console.log("BTC Log: Destroying video player");
								OO.destroy();
							}
							playVideo(comic, title, talent, playlist_id);
						});
					}
                },
                error: function (xml) {
                	$j('#btc-archive').hide();
                    console.log("BTC Log: Can't connect to video server to retrieve archive info");
                }
            });
        }

        function writeDivs(assetinfo) {
        	//If a specific playlist is passed via querystring, catch it and assign it
        	//to these variables to force a play on it.
        	var pv_playlistid = pl;

        	var pv = false;
        	var pv_comic = "";
        	var pv_title = "";
        	var pv_talent = "";

            var id = "";
            var rowcnt=0;
            var i = 0;
            var cnt = 0;
            var cols = 0;
            var colcnt = 1;
            var html = "";
            $j.each(assetinfo, function (key, val) {
            	if(pv_playlistid === val.playlist_id){
            		pv = true;
            		pv_comic = val.comic;
            		pv_talent = val.talent;
            	}

                id = cnt < 2 ? 'featured' : 'regular';
                cols = cnt < 2 ? 2 : 3;
                cnt++;
                colcnt = colcnt <= cols ? colcnt : 1;
                if(colcnt === 1) rowcnt++;

                var vid ="#vid-item" + cnt;
                var pl = "#playlist" + cnt;

                $j('#btc-content').on('click', vid, function () {
                	if(attribs.refresh) redirectToBTCPage(val.playlist_id);
                	else playVideo(val.comic,val.title, val.talent, val.playlist_id);
                });
                $j('#btc-content').on('click', pl, function () {
                    alert(val.playlist_id);
                });

                //Write the appropriate opening div tag
                if(colcnt === 1) html+= "<div class='"+ id +"'>";

                html += "<a href='#btc-main-container' id='vid-item" + cnt + "' class='vid-item'>";

                var now = new Date(),
            	midnight = new Date(now.getFullYear(),now.getMonth(), now.getDate(),0,0,0);
				if(new Date(val.updated_at) > midnight) html+= "<div class='btc-new-today'>New Today</div>";

				html += "<img src='" + val.thumb + "' alt='Beyond the Comics' /> \
                		<div class='caption'> \
                			<div class='title'>" + val.comic;
                var joke = val.title;
                var talent = val.talent;
                var max = 65;
            	if(val.comic.length + val.title.length > max){
                	var truncate = max - val.comic.length;
                	joke = joke.substring(0, truncate-3) + "...";
                }
                max = 26;
                if(val.talent.length > max){
                	talent = "<span style='font-size:12px;line-height:12px;'>" + talent + "</span>";
                	//talent = talent.substring(0, max-3) + "...";
                }
                if(rowcnt === 1) html += ":<div class='joke'>" + joke +"</div>";

                html += "</div><div class='voice'>" + talent + "</div> \
                			<div id='playlist" + cnt + "' class='playlist'>" + val.playlist_id + "</div> \
                		</div> \
                		</a>";
                //put spacing between each slide
                if(colcnt !== cols) html += "<div class='btc-colspace'></div>";

                //Place the ad on the second row in the second position;
                if(rowcnt === 2 && colcnt ===1 && attribs.style !== 'king-features') {
					html += "<div class='btc-ad-unit-small'> \
								<div class='ad-caption'>Advertisement</div> \
								<div id='btc-gridad'>";
					//Adsense Ad
					html += gridad;
					//DFP Ad
					//html += DFPAdCodeSlot1;

					html += "</div> \
							</div> \
							<div class='btc-colspace'></div>";
                	colcnt++;
                }

                //Write the ending div tag
                if(colcnt === cols) {
                	html += "</div>";
                	colcnt = 1;
                }else{
                	colcnt++;
                }
                rowdiv = id;
            });
			$j("#btc-content").html(html);
			console.log("BTC Log: page rendered.");
			var location = window.location.pathname;
            if (pv) playVideo(pv_comic, pv_title, pv_talent, pv_playlistid);
            else ga('send','pageview',location);
        }
        function playVideo(comic, title, talent, playlist_id) {
        	console.log("BTC Log: video playing");
        	//reinit globals
        	playing_video = true;
        	playing_pct = 0;
        	playing_comic = "";
        	playing_evt_title = "";
        	/*****************/
        	playing_comic = comic;
            var pv_playlistid = pl;
            var pv_hash = pv_playlistid===playlist_id?"#" + src + "_" + getInits(comic):"";
            var location = window.location.pathname + pv_hash;
            ga('send','pageview', location);
			ga('send', 'event', src + 'gridplaylist', 'opened', comic);
            $j('#btc-content').hide();
            $j('#pageblurb').hide();
            $j('#btc-gridad').html('');
            $j('#btc-playerad').html(playerad);
            $j("#btc-top-header").show();
            $j('#btc-player').show();
            $j('#playerinfo').show();
            $j('#pagetitle #comictitle').html(comic);
            $j('#pagetitle #comictalent').html('with ' + talent);
            console.log(xml)
            var player =      '<video id="movie" width="640" height="360" preload controls autoplay> \
                                   <source src="' + playlist_id + '.webm" type="video/webm; codecs=vp8,vorbis" /> \
                                   <source src="' + playlist_id + '.ogv" type="video/ogg; codecs=theora,vorbis" /> \
                                   <source src="' + playlist_id + '.mp4" /> \
                                   <object width="640" height="360" type="application/x-shockwave-flash" data="/assets/videos/flowplayer-3.2.1.swf"> \
                                        <param name="movie" value="/assets/videos/flowplayer-3.2.1.swf" /> \
                                        <param name="allowfullscreen" value="true" /> \
                                        <param name="flashvars" value="config={\'clip\': {\'url\': \'' + playlist_id + '.mp4\', \'autoPlay\':true, \'autoBuffering\':true}}" /> \
                                   </object> \
                              </video>';
           $j("#btc-playerContainer").html(player);
           console.log("height",$j("#btc-playerContainer").height());

            $j('#moreComicsButton').click(function () {
            	playing_video = false;
            	if(playing_evt_title == "") playing_evt_title = playing_comic + ": Noplay";
				ga('send', 'event', src + 'gridplaylist', 'aborted', playing_evt_title + '(BC)(' + getPlayThreshold(playing_pct) + ')');
                var location = window.location.pathname;
            	ga('send','pageview',location);
            	ga('send','event',src + 'homebutton','clicked','Back to Grid');
            	if(attribs.refresh){
            		redirectToBTCPage('home');
            	}else{
					$j('#btc-content').show();
					$j('#pageblurb').show();
					$j('#btc-gridad').html(gridad);
					$j('#btc-playerad').html('');
					$j('#btc-player').hide();
					$j('#playerinfo').hide();
					if (OO) {
						console.log("BTC Log: Destroying video player");
						OO.destroy();
					}
                }
            });
        }
        function getPlayDelay(delay){
        	var ret = "";
			if(delay <= 3) { ret = '<=3'; }
			else if(delay <=6) { ret = '<=6'; }
			else if(delay <= 9) { ret = '<=9'; }
			else { ret = '>9'; }
			return ret + 's';
        }
        function getPlayThreshold(pct){
        	var ret = "";
        	if(pct >= 75) {
        		ret = '>75%';
        	}else if(pct >= 50){
        		ret = '>50%';
        	}else if(pct >= 25){
        		ret = '>25%';
        	}else if(pct > 0){
        		ret = '<25%';
        	}else{
        		ret = '0%';
        	}
        	return ret;
        }
        function getUrlParameter(sParam)
		{
		     console.log(window.location);
			var sPageURL = window.location.search.substring(1);
			var sURLVariables = sPageURL.split('&');
			for (var i = 0; i < sURLVariables.length; i++)
			{
				var sParameterName = sURLVariables[i].split('=');
				if (sParameterName[0] == sParam)
				{
					return sParameterName[1];
				}
			}
			return 'undefined';
		}
		function redirectToBTCPage(url) {
		console.log("WHAT THE FUCK");
			var param = url === 'home' ? '' : '?PL=' + url + '#btc-edge-top';
			var protocol = window.location.protocol + '//';
			var hostname = window.location.hostname;
			var port = window.location.port != "" ? ":" + window.location.port : "";
			var pathname = window.location.pathname;
			url = protocol + hostname + port + pathname + param;
               window.location.replace(url);
        }
		function getInits(s){
			var re = /\b[A-Za-z]/g;
			var m;
			var ret = "";
			while ((m = re.exec(s)) != null) {
				ret += m;
				if (m.index === re.lastIndex) {
					re.lastIndex++;
				}
			}
			return ret.toLowerCase();
		}
		function clickit(elem) {
            if (!elem) {
                window.setTimeout(function () {
                    clickit(elem);
                }, 100);
            } else {
                elem.click();
            }
        }
    });
})();
