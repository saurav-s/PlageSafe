var app=angular.module('PlagsafeApp', ['ngRoute', 'ngFileUpload']);

app.config(function($routeProvider) {
    $routeProvider
        .when('/', {
        templateUrl: 'views/login.html'
        })
        .when('/upload', {
            resolve:{
                "check": function($location, $rootScope) {
                    if(!$rootScope.loggedIn) {
                        $location.path('/');
                    }
                }
            },
            templateUrl: 'views/upload.html'
        })
        .otherwise({
            redirectTo: '/'
        });
});

app.controller('LoginController', function($scope, $location, $rootScope, LoginService) {
    $scope.submit = function () {
        var username = $scope.username;
        var password = $scope.password;
        $rootScope.loggedIn = false;

        LoginService.findUser(username, password)
            .then(function(value) {
                var user = value.data;
                if(user != undefined && user.userName !== undefined) {
                    $rootScope.loggedIn = true;
                    $location.path('/upload');
                }
            });
    };
});

app.directive('fileModel', ['$parse', function ($parse) {
    return {
       restrict: 'A',
       link: function(scope, element, attrs) {
          var model = $parse(attrs.fileModel);
          var modelSetter = model.assign;
          
          element.bind('change', function(){
//             scope.$apply(function(){
//                modelSetter(scope, element[0].files[0]);
//             });
        	  	  scope.$apply(function(){
                  if (element[0].files.length > 1) {
                    modelSetter(scope, element[0].files);
                  }
                  else {
                    modelSetter(scope, element[0].files[0]);
                  }
                });
          });
       }
    };
}]); 

app.controller('uploadFileController', ['$scope', '$http', function($scope, $http){
    $scope.doUploadFile = function(){
       var fileList1 = $scope.uploadedFile1;
       var fileList2 = $scope.uploadedFile2;
       var url = "/api/uploadfile";
       
       var data = new FormData();
       data.append('uploadfile1', fileList1);
       data.append('uploadfile2', fileList2);
    
       var config = {
    	   	transformRequest: angular.identity,
    	   	transformResponse: angular.identity,
	   		headers : {
	   			'Content-Type': undefined
	   	    }
       }
       
       $http.post(url, data, config).then(function (response) {
			$scope.uploadResult=response.data;
		}, function (response) {
			$scope.uploadResult=response.data;
		});
    };
}]);

app.controller('getFilesController', ['$scope', '$http', function($scope, $http){
    $scope.doGetFiles = function(){
       var url = "/api/getallfiles";
       $http.get(url).then(function (response) {
			$scope.lstFiles = response.data;
		}, function (response) {
			alert(response.data);
		});
    };
}]);


app.controller('betaUploadFileController', ['$scope', '$http', 'Upload', '$timeout', function($scope, $http, Upload, $timeout){
  $scope.onFileSelect = function($fileList1, $fileList2) {
	  Upload.upload({
	    url: '/api/uploadfile',
	    data: {
	    		uploadfile1: $fileList1,
	    		uploadfile2: $fileList2
        }
	  }).then(function (response) {
          $timeout(function () {
              $scope.result = response.data;
          });
      }, function (response) {
          if (response.status > 0) {
              $scope.errorMsg = response.status + ': ' + response.data;
          }
      }, function (evt) {
          $scope.progress = 
              Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
      });
  };
  
  $scope.doBetaUploadFile = function($fileList1, $fileList2){
	  Upload.upload({
		    url: '/api/uploadfile',
		    data: {
		    		uploadfile1: $fileList1,
		    		uploadfile2: $fileList2
	        },
	        arrayKey: ''
		  }).then(function (response) {
	          $timeout(function () {
	              $scope.result = response.data;
	          });
	      }, function (response) {
	          if (response.status > 0) {
	              $scope.errorMsg = response.status + ': ' + response.data;
	          }
	      }, function (evt) {
	          $scope.progress = 
	              Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
	      });
	  };
  

}]);

