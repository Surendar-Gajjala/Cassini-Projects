<div>
    <style scoped>
        .found-no-results-container {
            position: absolute;
            top: 50px;
            bottom: 0;
            left: 0;
            right: 0;
        }

        .found-no-results-container > .no-results {
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            text-align: center;
            position: absolute;
            width: 100%;
        }

        .found-no-results-container > .no-results > img {
            width: 300px;
        }

        .found-no-results-container > .no-results > .no-results-message {
            font-size: 20px;
            font-weight: 400 !important;
        }

        .found-no-results-container > .no-results > .no-results-suggestion {
            font-size: 14px;
            font-weight: 300 !important;
        }
    </style>

    <div class="found-no-results-container">
        <div class="no-results">
            <img class="no-results-image" src="app/assets/images/no-results-found.png" alt="">
            <div class="no-results-message">Ooops...We did not find anything that matches this search :(</div>
            <div class="no-results-suggestion">Try searching for something else or check for spelling mistakes.</div>
        </div>
    </div>
</div>