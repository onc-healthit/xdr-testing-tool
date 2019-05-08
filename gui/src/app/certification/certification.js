var certMod = angular.module('ttt.certification', [
    // Modules
    'ttt.certification.certh1',
    'ttt.certification.certh2',
    'ttt.certification.certb1'
]);

certMod.config(['$stateProvider',
    function($stateProvider) {
        $stateProvider.state('certification', {
            url: '/certification',
				params: {
					paramsObj: null, paramCri: null
				},
            abstract: true,
            views: {
                "main": {
                    controller: 'CertificationCriteriaCtrl',
                    templateUrl: 'certification/certificationCriteria.tpl.html'
                }
            },
            data: {
                pageTitle: 'Certification'
            }
        })
            .state('certification.home', {
                url: '',
                views: {
                    "certification": {
                        controller: 'DirectHomeCtrl',
                        templateUrl: 'direct/direct-home/direct-home.tpl.html'
                    }
                }
            }).state('certification.certh2', {
                url: '/certh2',
                abstract: true,
                views: {
                    "certification": {
                        controller: 'Certh1Ctrl',
                        templateUrl: 'certification/certificationCriteriah1.tpl.html'
                    }
                },
                data: {
                    filterCrit: 'h2',
                    pageTitle:' 170.315(h)(2)'
                }
            })
            .state('certification.documents', {
                url: '/documents',
                views: {
                    "certification": {
                        controller: 'DocumentsCtrl',
                        templateUrl: 'templates/documents.tpl.html'
                    }
                }
            }).state('certification.register', {
				url: '/register',
				views: {
					"certification": {
						controller: 'RegisterCtrl',
						templateUrl: 'direct/register/register.tpl.html'
					}
				}
			})
			.state('certification.send', {
				url: '/send',
				views: {
					"certification": {
						controller: 'DirectSendCtrl',
						templateUrl: 'direct/send/send.tpl.html'
					}
				}
			})
			.state('certification.validator', {
				url: '/validator/direct',
				views: {
					"certification": {
						controller: 'DirectValidatorCtrl',
						templateUrl: 'direct/message-validator/direct/message-validator.tpl.html'
					}
				}
			})
			.state('certification.ccda', {
				url: '/validator/ccda',
				views: {
					"certification": {
						controller: 'CCDAValidatorCtrl',
						templateUrl: 'direct/message-validator/ccda/ccda-validator.tpl.html'
					}
				}
			})
			.state('certification.ccdar2', {
				url: '/validator/ccda/r2',
				views: {
					"certification": {
						controller: 'CCDAR2ValidatorCtrl',
						templateUrl: 'direct/message-validator/ccda-r2/ccda-r2.tpl.html'
					}
				}
			})
			.state('certification.dcdt1', {
				url: '/certdiscovery/dcdt1',
				views: {
					"certification": {
						controller: 'DCDTValidatorCtrl',
						templateUrl: 'direct/cert-discovery/dcdt.tpl.html'
					}
				},
                data: {
                    pageTitle:'2014'
                }
			})
			.state('certification.dcdt2', {
				url: '/certdiscovery/dcdt2',
				views: {
					"certification": {
						controller: 'DCDTValidatorCtrl',
						templateUrl: 'direct/cert-discovery/dcdt.tpl.html'
					}
				},
                data: {
                    pageTitle:'2015'
                }
			})
			.state('certification.status', {
				url: '/status',
				views: {
					"certification": {
						controller: 'DirectStatusCtrl',
						templateUrl: 'direct/status/status.tpl.html'
					}
				}
			})
			.state('certification.report', {
				url: '/report/:message_id',
				views: {
					"certification": {
						controller: 'DirectReportCtrl',
						templateUrl: 'direct/validation-report/validation-report.tpl.html'
					}
				}
			}).state('certification.xdm',{
			url: '/xdm',
			views: {
			"certification": {
			controller: 'XDMValidatorCtrl',
			templateUrl: 'message-validators/xdm-validator/xdm-validator.tpl.html'
			}
			}
			})
			.state('certification.xdr',{
			url: '/xdr',
			views: {
			"certification": {
			controller: 'XDRValidatorCtrl',
			templateUrl: 'message-validators/xdr-validator/xdr-validator.tpl.html'
			}
			}
			})
            .state('certification.faq', {
                url: '/faq',
                views: {
                    "certification": {
                        controller: 'MarkDownCtrl',
                        templateUrl: 'templates/faq.tpl.html'
                    }
                },
                data: {
                    moduleInfo: "faq"
                }
            }).state('certification.announcement', {
                url: '/announcement',
                views: {
                  "certification": {
                        controller: 'MarkDownCtrl',
                        templateUrl: 'templates/announcement.tpl.html'
                    }
                },
                data: {
                    moduleInfo: "announcement"
                }
            })
			.state('certification.releaseNotes', {
               url: '/releaseNotes',
                views: {
                    "certification": {
                        controller: 'ReleaseNotesCtrl',
                        templateUrl: 'templates/releaseNotes.tpl.html'
                    }
                }
            }).state('certification.help', {
                url: '/help',
                views: {
                    "certification": {
                        controller: 'CertificationCriteriaCtrl',
                        templateUrl: 'certification/help/help.tpl.html'
                    }
                }
            });

    }
]);

certMod.controller('CertificationCriteriaCtrl', ['$scope','$stateParams','PropertiesFactory',
    function($scope,$stateParams,PropertiesFactory) {

    $scope.paramsObj =  $stateParams.paramsObj;
$scope.backTo = null;
if ($stateParams.paramsObj !=null){
$scope.parmobj = "{paramCri:{'backToCriteria':"+$scope.paramsObj.backToCriteria+",'backToOption':"+$scope.paramsObj.backToOption+"}}";
$scope.backTo = $scope.paramsObj.goBackTo+"("+$scope.parmobj +")";
}

        PropertiesFactory.get(function(result) {
            $scope.properties = result;
        });
    }
]);