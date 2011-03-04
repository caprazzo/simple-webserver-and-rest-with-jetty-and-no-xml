if (!window.WebSocket)
	alert("WebSocket not supported by this browser");

function main() {

	var location = document.location.toString()
		.replace('http://', 'ws://')
		.replace('https://', 'wss://')
		.replace(".html", "");

	var ws = new WebSocket(location);

	ws.onopen = function() {

	}

	ws.onmessage = function(msg) {
		if (msg.data) {
			displayQuote(msg.data);
		}
	}

	ws.onclose = function() {

	}

	var quoteDiv = null;
	var count = 0;
	function displayQuote(quote) {

		var quotes = document.getElementById('quotes');
		quoteDiv = document.createElement('div');
		quoteDiv.className = "quote new";
		quoteDiv.innerHTML = quote;
		quotes.insertBefore(quoteDiv, quotes.firstChild);

		setTimeout(function() {
			quoteDiv.className = (count++ % 2 == 0) ? 'quote' : 'quote alt';
		}, 150);

		if (quotes.childNodes.length > 20) {
			quotes.removeChild(quotes.lastChild);
		}
	}
}