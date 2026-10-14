/**
 * Modern News Ticker
 * Customized for SVPNPA Portal
 */
(function ($) {
    var defaults = {
        effect: "scroll",
        autoplay: true,
        scrollInterval: 20,
        transitionTime: 500,
        feedType: "none",
        feedCount: 5,
        refresh: "10:00"
    };

    var methods = {
        init: function (options) {
            var settings = $.extend({}, defaults, options);

            return this.each(function () {
                var $ticker = $(this);
                var $label = $ticker.children(".latest-update, .mt-label");
                var $controls = $ticker.children(".mt-controls");
                var $news = $ticker.children(".mt-news");
                var $ul = $news.children("ul");
                var $prev = $controls.children(".mt-prev");
                var $next = $controls.children(".mt-next");
                var $play = $controls.children(".mt-play");

                if ($ticker.hasClass("mt-round")) {
                    $ticker.css("border-radius", "7px");
                }

                function getNewsWidth() {
                    var tickerWidth = $ticker.outerWidth() || $ticker.width();
                    var labelWidth = $label.length ? ($label.outerWidth(true) || 160) : 0;
                    var controlsWidth = $controls.length ? ($controls.outerWidth(true) || 90) : 0;
                    var calcWidth = tickerWidth - labelWidth - controlsWidth;
                    if (calcWidth < 50) calcWidth = 50;

                    $news.css({
                        "left": labelWidth + "px",
                        "width": calcWidth + "px",
                        "right": controlsWidth + "px"
                    });

                    return calcWidth;
                }

                function getTotalItemsWidth() {
                    var total = 0;
                    $ul.children("li").each(function () {
                        total += $(this).outerWidth(true);
                    });
                    return total;
                }

                var newsWidth = getNewsWidth();
                var totalItemsWidth = getTotalItemsWidth();

                // Ensure UL has enough width to prevent items from wrapping
                $ul.css({
                    "width": Math.max(totalItemsWidth + newsWidth * 2 + 1000, 4000) + "px",
                    "white-space": "nowrap",
                    "margin-left": newsWidth + "px" // Start from the RIGHT END of the news viewport
                });

                var isPlaying = settings.autoplay !== false;
                var isHovered = false;
                var timerId = null;

                function tick() {
                    if (!isPlaying || isHovered) return;

                    var currentMargin = parseFloat($ul.css("margin-left")) || 0;
                    currentMargin -= 1;

                    var currentNewsWidth = $news.width() || newsWidth;
                    var currentTotal = getTotalItemsWidth();

                    if (currentTotal < currentNewsWidth) {
                        // Short news / single item: scroll entirely across and restart from the right end
                        if (currentMargin < -currentTotal) {
                            currentMargin = currentNewsWidth;
                        }
                    } else {
                        // Longer news: continuous loop from right
                        var $first = $ul.children("li:first");
                        var firstWidth = $first.outerWidth(true);
                        if (currentMargin <= -firstWidth) {
                            $first.appendTo($ul);
                            currentMargin += firstWidth;
                        }
                    }

                    $ul.css("margin-left", currentMargin + "px");
                }

                function start() {
                    if (timerId) clearInterval(timerId);
                    timerId = setInterval(tick, settings.scrollInterval);
                    isPlaying = true;
                    $play.addClass("mt-pause");
                }

                function pause() {
                    if (timerId) {
                        clearInterval(timerId);
                        timerId = null;
                    }
                    isPlaying = false;
                    $play.removeClass("mt-pause");
                }

                if (settings.autoplay) {
                    start();
                }

                // Hover events to pause and resume
                $news.hover(
                    function () { isHovered = true; },
                    function () { isHovered = false; }
                );

                // Play / Pause button
                $play.off("click").on("click", function (e) {
                    e.preventDefault();
                    if (isPlaying) {
                        pause();
                    } else {
                        start();
                    }
                });

                // Next button: scroll faster left
                $next.off("click").on("click", function (e) {
                    e.preventDefault();
                    var currentMargin = parseFloat($ul.css("margin-left")) || 0;
                    var shift = 60;
                    var currentNewsWidth = $news.width() || newsWidth;
                    var currentTotal = getTotalItemsWidth();

                    if (currentTotal >= currentNewsWidth) {
                        var $first = $ul.children("li:first");
                        var firstWidth = $first.outerWidth(true);
                        if (currentMargin <= -firstWidth) {
                            $first.appendTo($ul);
                            currentMargin += firstWidth;
                        }
                    }
                    $ul.css("margin-left", (currentMargin - shift) + "px");
                });

                // Prev button: scroll right
                $prev.off("click").on("click", function (e) {
                    e.preventDefault();
                    var currentMargin = parseFloat($ul.css("margin-left")) || 0;
                    var shift = 60;
                    var currentNewsWidth = $news.width() || newsWidth;
                    if (currentMargin + shift > currentNewsWidth) {
                        currentMargin = currentNewsWidth;
                    } else {
                        currentMargin += shift;
                    }
                    $ul.css("margin-left", currentMargin + "px");
                });

                // Resize handler
                $(window).on("resize", function () {
                    newsWidth = getNewsWidth();
                });

                $ticker.data("pause", pause);
                $ticker.data("resume", start);
            });
        },
        pause: function () {
            return this.each(function () {
                var pauseFn = $(this).data("pause");
                if (pauseFn) pauseFn();
            });
        },
        resume: function () {
            return this.each(function () {
                var resumeFn = $(this).data("resume");
                if (resumeFn) resumeFn();
            });
        }
    };

    $.fn.modernTicker = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === "object" || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error("Method " + method + " does not exist on jQuery.modernTicker");
        }
    };
})(jQuery);