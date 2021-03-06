'use strict'

angular.module('ngApkreator').controller 'AppCreatorCtrl', ($scope, $rootScope, $compile, $http) ->
    # Set the App Creator menu item as the active one
    $rootScope.headerMenuItems = [
        { name: "Home", url: "#/", class: "" }
        { name: "App Creator", url: "#/app-creator", class: "active" }
        {name: "Features", url: "#/features", class: ""}
        { name: "Contact", url: "#", class: "" }
    ]

    # Initialize variables
    $scope.hasYoutube = true
    $scope.hasGplus = true
    $scope.hasTwitter = true
    $scope.hasWebsite = true
    $scope.features_text = ""

    # Initialize features variables
    $scope.features =
        youtube: localStorage.getItem "youtube"
        gplus: localStorage.getItem "gplus"
        twitter: localStorage.getItem "twitter"

    # Initialize config variables
    $scope.config =
        appName: localStorage.getItem "appName"
        packageName: localStorage.getItem "packageName"
        colorScheme: localStorage.getItem "colorScheme"
        icon: localStorage.getItem "icon"
        presentation: localStorage.getItem "presentation"
        website: localStorage.getItem "website"
        apiKey: localStorage.getItem "apiKey"

    # Adjust the API keys text to match the related features
    $scope.adjustFeaturesText = ->
        if $scope.hasYoutube && $scope.hasGplus
            $scope.features_text = "YouTube & Google+"
        else if $scope.hasYoutube
            $scope.features_text = "YouTube"
        else
            $scope.features_text = "Google+"

    # Validate the form and send the API request
    $scope.generateAPIRequest = ->
        saveToLocalStorage()

        request =
                "/app/" + encodeURIComponent($scope.config.appName) +
                "/package/" + encodeURIComponent($scope.config.packageName) +
                "/color/" + encodeURIComponent($scope.config.colorScheme) +
                "/icon/" + encodeURIComponent($scope.config.icon) +
                "/youtube/" + encodeURIComponent($scope.features.youtube) +
                "/gplus/" + encodeURIComponent($scope.features.gplus) +
                "/twitter/" + encodeURIComponent($scope.features.twitter) +
                "/facebook/unimplemented" + # Unimplemented features
                "/website/" + encodeURIComponent($scope.config.website) +
                "/welcome_title/Welcome!" + # TODO: add a card to let the user set the welcome card's title
                "/welcome_desc/" + encodeURIComponent($scope.config.presentation) +
                "/api_key/" + encodeURIComponent($scope.config.apiKey)

        console.log request

        vex.dialog.open({
            showCloseButton: false
            escapeButtonCloses: false
            overlayClosesOnClick: false
        }).html """
        <div class="sign-in-dialog">
             <div class="text-center">
                 <h1 class="teal no-margin-top">Building Your App</h1>
             </div>
             <div class="csspinner bar-follow" style="width: 450px; margin-top: 30px; margin-bottom: 80px;"></div>
        </div>
        """

        $http.get('http://localhost:5000' + request, {timeout: 600000})
        .success((data, status, headers, config) ->
            console.log data, status, headers, config
            vex.closeAll()
            window.location = data.apkUrl

        ).error((data, status, headers, config) ->
            console.log "error", data, status, headers, config
            vex.dialog.open().html """
            <div class="sign-in-dialog">
                 <div class="text-center">
                     <h1 class="pumpkin no-margin-top">There Was an Error!</h1>
                 </div>
                 <p class="aleo">Sorry, an error seems to have occured while building your app. Please try again.</p>
            </div>
            """
        )


    $scope.adjustFeaturesText()

    $scope.compileHtml = (html) ->
        linkThat = $compile html
        return linkThat $scope

    # Save the user input to localStorage
    saveToLocalStorage = () ->
        localStorage.setItem "appName", $scope.config.appName
        localStorage.setItem "packageName", $scope.config.packageName
        localStorage.setItem "colorScheme", $scope.config.colorScheme
        localStorage.setItem "icon", $scope.config.icon
        localStorage.setItem "presentation", $scope.config.presentation
        localStorage.setItem "apiKey", $scope.config.apiKey
        localStorage.setItem "hasYoutube", $scope.hasYoutube
        localStorage.setItem "youtube", $scope.features.youtube
        localStorage.setItem "twitter", $scope.features.twitter
        localStorage.setItem "hasGplus", $scope.hasGplus
        localStorage.setItem "gplus", $scope.features.gplus
        localStorage.setItem "website", $scope.config.website

