/**
 * @license Copyright (c) 2003-2023, CKSource Holding sp. z o.o. All rights reserved.
 * For licensing, see https://ckeditor.com/legal/ckeditor-oss-license
 */

// Load Google Font 'Noto Sans' into all CKEditor instances
CKEDITOR.addCss(
	"@import url('https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap');" +
	"body, .cke_editable, p, div, span, h1, h2, h3, h4, h5, h6, table, td, th, li, a { font-family: 'Noto Sans', sans-serif !important; }"
);

CKEDITOR.editorConfig = function( config ) {
	// Set Noto Sans as default and exclusive font family as per DBIM
	config.font_defaultLabel = 'Noto Sans';
	config.font_names = 'Noto Sans/Noto Sans, sans-serif;';

	// Load Noto Sans font stylesheet in CKEditor content area
	config.contentsCss = [
		'https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap',
		CKEDITOR.getUrl('contents.css')
	];

	// Automatically clean pasted font styles (e.g. from MS Word) to maintain Noto Sans consistency
	config.pasteFromWordRemoveFontStyles = true;
};
