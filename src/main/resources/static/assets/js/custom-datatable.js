/**
 * Custom DataTables UI Enhancement
 * SVPNPA Project
 * Matches modern UI specifications:
 * - Magnifying glass icon & placeholder inside search bar
 * - Right aligned controls: [ ≡ | Sort by ∨ ] and [ ☷ X per page ∨ ]
 * - Auto-detects table headers to populate Sort By options
 */
(function($) {
    if (!$) return;

    function applyDataTableDefaults() {
        if ($.fn && $.fn.dataTable && $.fn.dataTable.defaults) {
            $.extend(true, $.fn.dataTable.defaults, {
                language: {
                    search: "",
                    searchPlaceholder: "Search...",
                    lengthMenu: "_MENU_ per page"
                }
            });
        }
    }

    applyDataTableDefaults();
    $(document).ready(applyDataTableDefaults);

    $(document).on('init.dt', function(e, settings) {
        try {
            var api = new $.fn.dataTable.Api(settings);
            var $wrapper = $(settings.nTable).closest('.dataTables_wrapper');

            if ($wrapper.find('.dt-sort-control').length > 0) return;

            // Format length options to "X per page"
            $wrapper.find('.dataTables_length select option').each(function() {
                var val = $(this).val();
                if (val && !$(this).text().includes('per page')) {
                    $(this).text(val + ' per page');
                }
            });

            // Build Sort By dropdown dynamically from visible table column headers
            var $sortSelect = $('<select class="dt-sort-select" aria-label="Sort by"><option value="">Sort by</option></select>');
            api.columns().every(function(index) {
                var headerText = $(this.header()).text().trim();
                if (headerText) {
                    $sortSelect.append($('<option></option>').attr('value', index).text(headerText));
                }
            });

            var $sortControl = $('<div class="dt-sort-control">' +
                '<span class="dt-sort-icon"><i class="fa-solid fa-bars-staggered"></i></span>' +
                '</div>');
            $sortControl.append($sortSelect);

            var $length = $wrapper.find('.dataTables_length');
            var $lengthParent = $length.parent();

            // Group Sort by and Length select inside a common container
            var $controlsGroup = $('<div class="dataTables_length_container"></div>');
            $controlsGroup.append($sortControl);
            $controlsGroup.append($length);
            $lengthParent.append($controlsGroup);

            // Handle Sort By change event
            $sortSelect.on('change', function() {
                var colIdx = $(this).val();
                if (colIdx !== "") {
                    api.order([parseInt(colIdx, 10), 'asc']).draw();
                }
            });
        } catch (err) {
            console.error("Custom DataTables initialization error:", err);
        }
    });
})(jQuery);
