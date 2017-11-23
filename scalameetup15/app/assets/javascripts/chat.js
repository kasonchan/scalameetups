$(document).ready(function () {
  // Hide error message
  $("#div-error").hide();
})

/**
 * Sticky relocate
 * Add stick class when it set off the sticky-anchor
 * Otherwise remove stick class
 */
function sticky_relocate() {
  var window_top = $(window).scrollTop();
  var div_top = $('#sticky-anchor').offset().top;
  if (window_top > div_top) {
    $('#div-msgs-input').addClass('stick');
  } else {
    $('#div-msgs-input').removeClass('stick');
  }
}

$(function () {
  $(window).scroll(sticky_relocate);
  sticky_relocate();
});
