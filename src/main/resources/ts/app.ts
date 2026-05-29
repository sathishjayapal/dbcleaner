import 'bootstrap';
import htmx from 'htmx.org';
import 'scss/app.scss';


/**
 * Register an event at the document for the specified selector,
 * so events are still catched after DOM changes.
 */
function handleEvent(eventType: string, selector: string, handler: (event:Event) => any) {
  document.addEventListener(eventType, function(event) {
    const eventTarget = event.target! as Element;
    if (eventTarget.matches(selector + ', ' + selector + ' *')) {
      handler.apply(eventTarget.closest(selector), arguments);
    }
  });
}

handleEvent('change', '.js-selectlinks', function() {
  htmx.ajax('get', this.value, document.body);
  history.pushState({ htmx: true }, '', this.value);
});
