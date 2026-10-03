'use strict';
ajssample.controller('samplePageController', ['$scope', '$http', '$rootScope', '$window', '$filter'
            , function ($scope, $http, $rootScope, $window, $filter) {

                $scope.regDetails = {};
                $scope.editRegDetails = {};

                $(document).ready(function () {
                    $.ajax({
                        type: "POST",
                        url: "signuppage.do?mode=getUserRegDetails",
                        //            enctype: 'multipart/form-data',
                        dataType: 'json',
                        contentType: false,
                        processData: false,
                        data: function () {
                            var data = new FormData();
                            return data;
                        }(),
                        success: function (data, status) {
                            // alert(data.records);
                            $scope.listofregdetails = data.records;
                            $scope.$apply();
                           // jQuery.noConflict();
                        }
                    });
                }); 

                $scope.tastData = function () {

                    $.ajax({
                        type: "POST",
                        url: "signuppage.do?mode=saveSigndataAjax",
//            enctype: 'multipart/form-data',
                        dataType: 'json',
                        contentType: false,
                        processData: false,
                        data: function () {
                            var data = new FormData();
                            data.append('property(userid)', $scope.regDetails.userid);
                            data.append('property(name)', $scope.regDetails.name);
                            data.append('property(uname)', $scope.regDetails.uname);
                            data.append('property(pwd)', $scope.regDetails.pwd);
                            data.append('property(email)', $scope.regDetails.email);
                            return data;
                        }(),
                        success: function (data, status) {
                            // alert(data.records);
                            $scope.successMessage = data.success;
                            $scope.listofregdetails = data.records;
                            $scope.regDetails = {};
                            $scope.$apply();
                           // jQuery.noConflict();
                        }
                    });
                };

                $scope.editUserDetails = function (userid, type) {
                    alert(type + "---hi---" + userid);
                    $scope.userid = userid;
                    $.ajax({
                        type: "POST",
                        url: "signuppage.do?mode=getUserRegDetailsEditorUpdate",
//            enctype: 'multipart/form-data',
                        dataType: 'json',
                        contentType: false,
                        processData: false,
                        data: function () {
                            var data = new FormData();
                            data.append('property(type)', type);
                            data.append('property(userid)', userid);
                            if (type = 'update') {
                                data.append('property(name)', $scope.editRegDetails.name);
                                data.append('property(pwd)', $scope.editRegDetails.pwd);
                                data.append('property(email)', $scope.editRegDetails.email);
                            }
//                
                            return data;
                        }(),
                        success: function (data, status) {
                            if (data.type == 'edit') {
                                $scope.editRegDetails = data;
                            }
                            if (data.type == 'update') {
                                $scope.successMessage = data.successupdate;
                                $scope.listofregdetails = data.records;
                                $('#myModalforEdituserDetails').hide();
                            }
                            if (data.type == 'delete') {
                                $scope.successMessage = data.successdelete;
                                $scope.listofregdetails = data.records;
                            }
                            $scope.$apply();
                           // jQuery.noConflict();
                        }
                    });
                };



  $scope.editUserDetails1 = function (srcode, type) {
                    alert(type + "---hi---" + srcode);
                    $scope.srcode = srcode;
                    $.ajax({
                        type: "POST",
                        url: "signuppage.do?mode=getUserRegDetailsEditorUpdate",
//            enctype: 'multipart/form-data',
                        dataType: 'json',
                        contentType: false,
                        processData: false,
                        data: function () {
                            var data = new FormData();
                            data.append('property(type)', type);
                            data.append('property(userid)', srcode);
                            if (type = 'update') {
                                data.append('property(name)', $scope.editRegDetails.name);
                                data.append('property(pwd)', $scope.editRegDetails.pwd);
                                data.append('property(email)', $scope.editRegDetails.email);
                            }
//                
                            return data;
                        }(),
                        success: function (data, status) {
                            if (data.type == 'edit') {
                                $scope.editRegDetails = data;
                            }
                            if (data.type == 'update') {
                                $scope.successMessage = data.successupdate;
                                $scope.listofregdetails = data.records;
                                $('#myModalforEdituserDetails').hide();
                            }
                            if (data.type == 'delete') {
                                $scope.successMessage = data.successdelete;
                                $scope.listofregdetails = data.records;
                            }
                            $scope.$apply();
                           // jQuery.noConflict();
                        }
                    });
                };


            }]);

