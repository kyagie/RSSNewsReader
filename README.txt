The Code Consists Of Three Activities and three Layouts.

1.The Landing Activity/Layout
The Landing Activity/Layout is the first layout shown to the user. It has a text Input that Lets you type a URL. The URL is validated to verify that it matches the url criteria.
When the Get Feeds Button is typed, It checks that url validation is true then parses the URL to the next Activity.

2.The Main Activity/Layout
The Main Actity/Layout Loads and downloads XML from the provided URL. The list downloaded is Iterated through and the title tag that contains the Headline is parsed to a textview.
The Textview is clickable and when clicked opens the details Activity that contains more information about the Headline.

3.The News Details Activity/Layout
This Activity loads the URL passed from the Main Activity in a webview component, loading pictures and links.



