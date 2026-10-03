(function() {
  "use strict";

  /**
   * Easy selector helper function
   */
  const select = (el, all = false) => {
    el = el.trim()
    if (all) {
      return [...document.querySelectorAll(el)]
    } else {
      return document.querySelector(el)
    }
  }

  /**
   * Easy event listener function
   */
  const on = (type, el, listener, all = false) => {
    let selectEl = select(el, all)
    if (selectEl) {
      if (all) {
        selectEl.forEach(e => e.addEventListener(type, listener))
      } else {
        selectEl.addEventListener(type, listener)
      }
    }
  }

  /**
   * Easy on scroll event listener 
   */
  const onscroll = (el, listener) => {
    el.addEventListener('scroll', listener)
  }

  /**
   * Navbar links active state on scroll
   */
  let navbarlinks = select('#navbar .scrollto', true)
  const navbarlinksActive = () => {
    let position = window.scrollY + 200
    navbarlinks.forEach(navbarlink => {
      if (!navbarlink.hash) return
      let section = select(navbarlink.hash)
      if (!section) return
      if (position >= section.offsetTop && position <= (section.offsetTop + section.offsetHeight)) {
        navbarlink.classList.add('active')
      } else {
        navbarlink.classList.remove('active')
      }
    })
  }
  window.addEventListener('load', navbarlinksActive)
  onscroll(document, navbarlinksActive)

  /**
   * Scrolls to an element with header offset
   */
  const scrollto = (el) => {
    let header = select('#header')
    let offset = header.offsetHeight

    if (!header.classList.contains('header-scrolled')) {
      offset -= 16
    }

    let elementPos = select(el).offsetTop
    window.scrollTo({
      top: elementPos - offset,
      behavior: 'smooth'
    })
  }

  /**
   * Toggle .header-scrolled class to #header when page is scrolled
   */
  let selectHeader = select('#header')
  if (selectHeader) {
    const headerScrolled = () => {
      if (window.scrollY > 100) {
        selectHeader.classList.add('header-scrolled')
      } else {
        selectHeader.classList.remove('header-scrolled')
      }
    }
    window.addEventListener('load', headerScrolled)
    onscroll(document, headerScrolled)
  }

  /**
   * Back to top button
   */
  let backtotop = select('.back-to-top')
  if (backtotop) {
    const toggleBacktotop = () => {
      if (window.scrollY > 100) {
        backtotop.classList.add('active')
      } else {
        backtotop.classList.remove('active')
      }
    }
    window.addEventListener('load', toggleBacktotop)
    onscroll(document, toggleBacktotop)
  }

  /**
   * Mobile nav toggle
   */
  on('click', '.mobile-nav-toggle', function(e) {
    select('#navbar').classList.toggle('navbar-mobile')
    this.classList.toggle('bi-list')
    this.classList.toggle('bi-x')
  })

  /**
   * Mobile nav dropdowns activate
   */
  on('click', '.navbar .dropdown > a', function(e) {
    if (select('#navbar').classList.contains('navbar-mobile')) {
      e.preventDefault()
      this.nextElementSibling.classList.toggle('dropdown-active')
    }
  }, true)

  /**
   * Scrool with ofset on links with a class name .scrollto
   */
  on('click', '.scrollto', function(e) {
    if (select(this.hash)) {
      e.preventDefault()

      let navbar = select('#navbar')
      if (navbar.classList.contains('navbar-mobile')) {
        navbar.classList.remove('navbar-mobile')
        let navbarToggle = select('.mobile-nav-toggle')
        navbarToggle.classList.toggle('bi-list')
        navbarToggle.classList.toggle('bi-x')
      }
      scrollto(this.hash)
    }
  }, true)

  /**
   * Scroll with ofset on page load with hash links in the url
   */
  window.addEventListener('load', () => {
    if (window.location.hash) {
      if (select(window.location.hash)) {
        scrollto(window.location.hash)
      }
    }
  });
  /**
   * Clients Slider
   */
  new Swiper('.clients-slider', {
    speed: 400,
    loop: true,
    autoplay: {
      delay: 5000,
      disableOnInteraction: false
    },
    slidesPerView: 'auto',
    pagination: {
      el: '.swiper-pagination',
      type: 'bullets',
      clickable: true
    },
    breakpoints: {
      320: {
        slidesPerView: 2,
        spaceBetween: 40
      },
      480: {
        slidesPerView: 3,
        spaceBetween: 60
      },
      640: {
        slidesPerView: 4,
        spaceBetween: 80
      },
      992: {
        slidesPerView: 6,
        spaceBetween: 120
      }
    }
  });

  /**
   * Porfolio isotope and filter
   */
  window.addEventListener('load', () => {
    let portfolioContainer = select('.portfolio-container');
    if (portfolioContainer) {
      let portfolioIsotope = new Isotope(portfolioContainer, {
        itemSelector: '.portfolio-item',
        layoutMode: 'fitRows'
      });

      let portfolioFilters = select('#portfolio-flters li', true);

      on('click', '#portfolio-flters li', function(e) {
        e.preventDefault();
        portfolioFilters.forEach(function(el) {
          el.classList.remove('filter-active');
        });
        this.classList.add('filter-active');

        portfolioIsotope.arrange({
          filter: this.getAttribute('data-filter')
        });
        portfolioIsotope.on('arrangeComplete', function() {
          AOS.refresh()
        });
      }, true);
    }

  });

  /**
   * Initiate portfolio lightbox 
   */
  const portfolioLightbox = GLightbox({
    selector: '.portfolio-lightbox'
  });

  /**
   * Portfolio details slider
   */
  new Swiper('.portfolio-details-slider', {
    speed: 400,
    loop: true,
    autoplay: {
      delay: 5000,
      disableOnInteraction: false
    },
    pagination: {
      el: '.swiper-pagination',
      type: 'bullets',
      clickable: true
    }
  });

  /**
   * Testimonials slider
   */
  new Swiper('.testimonials-slider', {
    speed: 600,
    loop: true,
    autoplay: {
      delay: 5000,
      disableOnInteraction: false
    },
    slidesPerView: 'auto',
    pagination: {
      el: '.swiper-pagination',
      type: 'bullets',
      clickable: true
    },
    breakpoints: {
      320: {
        slidesPerView: 1,
        spaceBetween: 20
      },

      1200: {
        slidesPerView: 3,
        spaceBetween: 20
      }
    }
  });

  /**
   * Animation on scroll
   */
  window.addEventListener('load', () => {
    AOS.init({
      duration: 1000,
      easing: 'ease-in-out',
      once: true,
      mirror: false
    })
  });

})()




  //No color//
  $(".nocolor").click(function () {
    $("body").css({ "-moz-filter": "grayscale(100%)", "-webkit-filter": "grayscale(100%)", "filter": "gray", "filter": "grayscale(100%)" });
    $("body").css({ "background-color": "#000", "color": "#fff" });
    $("h1, h2, h3, h4, h5, h6").css({ "color": "#000" });
    $(".menu_nav").css({ "background-color": "#000", "color": "#fff" });
    $("#footer div div div p a").css({ "color": "#aaa" });
    $("#footer div div div.footer-links h6").css({ "color": "#aaa" });
    $("#footer div div div.footer-links h4").css({ "color": "#fff" });
    $("#footer div div div.footer-newsletter h6").css({ "color": "#aaa" });  
    $(".contact h4, .contact h6").css({ "color": "#fff" });   
    $(".footer-links h4").css({ "color": "#d6dc2d" });   
    $(".footer-links h6").css({ "color": "#d6dc2d" }); 
    $(".social-links h6").css({ "color": "#fff" });   
    $(".about .content h3").css({ "color": "#fff" });  
    $(".about .content h5").css({ "color": "#fff" });   
    $("#header h1").css({ "color": "#fff" });   
    $(".navbar").css({ "background": "#333" }); 
  });

  //redcolor//  
  $(".redcolor").click(function () {
    $("body").css({ "-moz-filter": "grayscale(0%)", "-webkit-filter": "grayscale(0%)", "filter": "white", "filter": "grayscale(0%)" });
    $("body").css({ "background-color": "#fff", "color": "#111" });
    $(".menu_nav").css({ "background-color": "#D82148", "color": "#fff" });
    $(".events marquee").css({ "color": "#111" });
    $("table tbody tr td").css({ "color": "#111" });
    $("form label").css({ "color": "#111" });
    $("form select").css({ "color": "#111" });
    $("form select option").css({ "color": "#111" });
    $(".tablearea p").css({ "color": "#111" });
    $(".forms").css({ "color": "#111" });    
    $("#header h1").css({ "color": "#D82148" });   
    $(".contact h4").css({ "color": "#fff" });     
    $(".contact").css({ "background": "#31439b", "color": "#9ea7d1" });  
    $(".social-links h6").css({ "color": "#fff" });  
    $(".navbar").css({ "background": "#D82148" }); 
  });


  //greencolor//  
  $(".greencolor").click(function () {
    $("body").css({ "-moz-filter": "grayscale(0%)", "-webkit-filter": "grayscale(0%)", "filter": "white", "filter": "grayscale(0%)" });
    $("body").css({ "background-color": "#fff", "color": "#111" });
    $(".menu_nav").css({ "background-color": "#288d3b", "color": "#fff" });
    $(".events marquee").css({ "color": "#111" });
    $("table tbody tr td").css({ "color": "#111" });
    $("form label").css({ "color": "#111" });
    $("form select").css({ "color": "#111" });
    $("form select option").css({ "color": "#111" });
    $(".tablearea p").css({ "color": "#111" });
    $(".forms").css({ "color": "#111" });    
    $("#header h1").css({ "color": "#0e5a1c" });    
    $(".links h4").css({ "color": "#0e5a1c" });    
    $(".acad_events h4").css({ "color": "#0e5a1c" });    
    $(".moduletable h4").css({ "color": "#0e5a1c" });     
    $(".up_coming h4").css({ "color": "#0e5a1c" });      
    $(".contact").css({ "background": "#0e5a1c", "color": "#65cb6b" });  
    $(".navbar .dropdown ul a").css({ "color": "#0e5a1c" });       
    $(".navbar .dropdown ul a:hover, .navbar .dropdown ul .active:hover, .navbar .dropdown ul li:hover > a").css({ "background": "#fff", "color": "#65cb6b" });      
    $(".social-links h6").css({ "color": "#fff" });  
    $(".navbar").css({ "background": "#0e5a1c" }); 
  });



  //bluecolor//  
  $(".bluecolor").click(function () {
    $("body").css({ "-moz-filter": "grayscale(0%)", "-webkit-filter": "grayscale(0%)", "filter": "white", "filter": "grayscale(0%)" });
    $("body").css({ "background-color": "#fff", "color": "#111" });
    $(".menu_nav").css({ "background-color": "#2097dd", "color": "#fff" });
    $(".events marquee").css({ "color": "#111" });
    $("table tbody tr td").css({ "color": "#111" });
    $("form label").css({ "color": "#111" });
    $("form select").css({ "color": "#111" });
    $("form select option").css({ "color": "#111" });
    $(".tablearea p").css({ "color": "#111" });
    $(".forms").css({ "color": "#111" });    
    $("#header h1").css({ "color": "#006eaf" });     
    $(".links h4").css({ "color": "#0d6eaf" });    
    $(".acad_events h4").css({ "color": "#0d6eaf" });    
    $(".moduletable h4").css({ "color": "#0d6eaf" });     
    $(".up_coming h4").css({ "color": "#0d6eaf" });      
    $(".contact").css({ "background": "#0d6eaf", "color": "#abd1eb" });  
    $(".navbar .dropdown ul a").css({ "color": "#0d6eaf" });           
    $(".navbar .dropdown ul a:hover, .navbar .dropdown ul .active:hover, .navbar .dropdown ul li:hover > a").css({ "background": "#fff", "color": "#0d6eaf" });       
    $(".social-links h6").css({ "color": "#fff" });   
    $(".navbar").css({ "background": "#0d6eaf" });     
  });






  var $affectedElements = $("*");
  $affectedElements.each(function () {
  var $this = $(this);
  $this.data("orig-size", $this.css("font-size"));
  });


  //Increase Font Size//  
  $("#btn-increase").click(function () {
    $("h1").css("font-size", "42px");
    $("h2").css("font-size", "36px");
    $("h3").css("font-size", "32px");
    $("h4").css("font-size", "28px");
    $("h5").css("font-size", "22px");
    $("h6").css("font-size", "18px");
    $("p, a, li, td, th, span, label").css("font-size", "16px");
    $(".events span:first-child h2").css("font-size", "24px");
    $(".events span:first-child h2").css("width", "30%");
    $(".info_service h2, .toll h1, .toll h2").css("font-size", "24px");
    $(".info_service div div.serviceBox7 marquee div div h4").css("font-size", "24px !important");
    $(".serviceBox1, .serviceBox4, .serviceBox6, .serviceBox7").css("min-height", "300px");
    $(".info_service div div.serviceBox7 marquee").css("max-height", "260px"); 
      $(".upcoming_events div div div:last-child a").css("font-size", "28px");
  })


  //Decrease Font Size//
  $("#btn-decrease").click(function () {
    $("p, a, li, td, th, span, label").css("font-size", "12px");
    $("h1, h2, h3, h4, h5, h6").css("font-size", "20px");
    $(".header").css("font-size", "20px");
    $(".header div:nth-child(2) div div:nth-child(2) h1").css("font-size", "50px");
      $(".upcoming_events div div div:last-child a").css("font-size", "20px");
  })


  //Orignal Font Size//
  $("#btn-orig").click(function () {
    $affectedElements.each(function () { var $this = $(this);
    // $("h1, h2, h3, h4, h5, h6").css("font-size", "22px");
    // $("h1").css("font-size", "36px");
      $this.css("font-size", "14px", $this.data("orig-size"));
      $("h1").css("font-size", "36px");
      $("h2").css("font-size", "30px");
      $("h3").css("font-size", "28px");
      $("h4").css("font-size", "24px");
      $("h5").css("font-size", "18px");
      $("h6").css("font-size", "16px");
      $(".upcoming_events div div div:last-child a").css("font-size", "26px");
      
    });
  })


  function changeFontSize(direction) {
  $affectedElements.each(function () {
  var $this = $(this);
  $this.css("font-size", parseInt($this.css("font-size")) + direction);
  });
  }


