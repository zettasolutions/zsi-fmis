  (function(){
        
    var  bs         = zsi.bs.ctrl
        ,svn        = zsi.setValIfNull
        ,gVehicleId   = null
        ,gActiveTab = ""
    ;
    
    zsi.ready = function(){
        $(".page-title").html("Dashboard");
        displayVehicles(); 
        gActiveTab = "vehicles";
        
        $("#vehicleId").select2({placeholder: "VEHICLES",allowClear: true});
        $('a[data-toggle="tab"]').on('shown.bs.tab', function(e){
            var target = $(e.target).attr("href"); 
            switch(target){
                case "#nav-vehicles":
                    gActiveTab = "vehicles";
                    $("#searchVal").val("");
                    $("#vehicleDiv").addClass("hide");
                    $("#dummyDiv").removeClass("hide");
                    $("#searchDiv").removeClass("hide");
                    $("#dummyDivSearch").addClass("hide");
                    break;
                case "#nav-fuel":
                    gActiveTab = "fuel";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    $("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayRefuelTransactions(gVehicleId);
                    break;
                case "#nav-pms":
                    gActiveTab = "pms";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    $("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayPMS(gVehicleId);
                    break;
                case "#nav-accidents":
                    gActiveTab = "accidents";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    $("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayAccidentTransactions(gVehicleId);
                    break;
                case "#nav-repairs":
                    gActiveTab = "repairs";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    $("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayRepairs(gVehicleId);
                    break;
                case "#nav-safety-problems":
                    gActiveTab = "safety-problems";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    $("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displaySafetyProblems(gVehicleId);
                    break;
              default:break;
            } 
        }); 
        
    }; 
    
    function displaySelects(){
        $("#vehicleId").dataBind({
             sqlCode    : "D231" //dd_vehicle_sel
            ,text       : "plate_no"
            ,value      : "vehicle_id" 
            ,required   : true
            ,onChange   : function(){ 
                gVehicleId = this.val();  
            }
        });
    }
    
    function displayVehicles(searchVal){  
        $("#gridVehicles").dataBind({
             sqlCode            : "T237" //transaction_vehicles_sel
            ,parameters         : {search_val:(searchVal ? searchVal : "")}
	        ,height             : $(window).height() - 241 
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:"Plate No"                                                                                    ,width:200       ,style:"text-align:left"
                        ,onRender  :  function(d){ 
                                   return app.bs({name:"vehicle_id"             ,type:"hidden"      ,value: app.svn(d,"vehicle_id")})
                                        + app.bs({name:"is_edited"              ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        + app.bs({name:"plate_no"               ,type:"input"       ,value: app.svn(d,"plate_no")});
                                        
                        }
                    }
                ] 
                ,onComplete : function(o){
                    var _dRows = o.data.rows;
                    var _this  = this;
        	        var _zRow  = _this.find(".zRow");
        	        if(_dRows.length < 1) {
        	                $("#nav-tab").find("[aria-controls='nav-fuel']").hide();
            	            $("#nav-tab").find("[aria-controls='nav-pms']").hide();
            	            $("#nav-tab").find("[aria-controls='nav-accidents']").hide();
            	            $("#nav-tab").find("[aria-controls='nav-repairs']").hide();
            	            $("#nav-tab").find("[aria-controls='nav-safety-problems']").hide();
        	            }
        	        _zRow.unbind().click(function(){
        	            var _self=this;
        	            setTimeout(function(){ 
            	            var _i      = $(_self).index();
            	            var _data   = _dRows[_i];
            	            var _vehicleId  = _data.vehicle_id;
            	            gVehicleId = _vehicleId;
            	            displaySelects();
            	            $("#nav-tab").find("[aria-controls='nav-fuel']").show();
            	            $("#nav-tab").find("[aria-controls='nav-pms']").show();
            	            $("#nav-tab").find("[aria-controls='nav-accidents']").show();
            	            $("#nav-tab").find("[aria-controls='nav-repairs']").show();
            	            $("#nav-tab").find("[aria-controls='nav-safety-problems']").show();
            	            setTimeout(function(){
            	                $("#vehicleId").val(_vehicleId).trigger('change');
            	            }, 200);
                            //displayVehicles();

        	            }, 200);
        	        });
        	        _this.on('dragstart', function () {
                        return false;
                    });
                    _this.find("input").attr("readonly", true); 
                    
                } 
            });
        }
        
    function displayRefuelTransactions(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridRefuel").dataBind({
             sqlCode            : "R216" //refuel_transactions_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 235
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"refuel_id"   ,type:"hidden"      ,value: app.svn(d,"refuel_id")}) 
                                        + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }   
                    ,{text:"Doc No"                         ,type:"input"          ,name:"doc_no"                    ,width:100       ,style:"text-align:left"}
                    ,{text:"Doc Date"                       ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"doc_date"     ,type:"input"      ,value: app.svn(d,"doc_date").toShortDate()})
                                 + app.bs({name:"vehicle_id"   ,type:"hidden"     ,value: vehicle_id}); 
                        }
                    } 
                    ,{text:"Driver"                         ,type:"select"          ,name:"driver_id"                 ,width:150       ,style:"text-align:left"}
                    ,{text:"Pao"                            ,type:"select"          ,name:"pao_id"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Odo Reading"                    ,type:"input"           ,name:"odo_reading"               ,width:150       ,style:"text-align:left"}
                    ,{text:"Gas Station"                    ,type:"select"          ,name:"gas_station_id"            ,width:100       ,style:"text-align:left"}
                    ,{text:"Number of Liters"                                                                         ,width:100       ,style:"text-align:center"
                        ,onRender: function(d){
                            return app.bs({name: "no_liters"             ,type: "input"     ,value: app.svn(d,"no_liters")         ,style : "text-align:center;"});
                        }
                    }
                    ,{text:"Unit Price"                                                                              ,width:70        ,style:"text-align:right;padding-right: 0.3rem;"
                        ,onRender: function(d){
                            return app.bs({name: "unit_price"             ,type: "input"     ,value: app.svn(d,"unit_price") !=="" ? app.svn(d,"unit_price").toMoney() : app.svn(d,"unit_price")         ,style : "text-align:right;padding-right: 0.3rem;"});
                        }
                    }
                    ,{text:"Refuel Amount"                                                                           ,width:90       ,style:"text-align:right;padding-right: 0.3rem;"
                        ,onRender: function(d){
                            return app.bs({name: "refuel_amount"          ,type: "input"     ,value: app.svn(d,"refuel_amount") !=="" ? app.svn(d,"refuel_amount").toMoney() : app.svn(d,"refuel_amount")      ,style : "text-align:right;padding-right: 0.3rem;"});
                        }
                    }
                    /*,{text:"Is Posted"                      ,type:"yesno"          ,name:"is_posted"                 ,width:70       ,style:"text-align:left" ,defaultValue:"N"} 
                    ,{text:"Posted Date"                    ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"posted_date"     ,type:"input"      ,value: svn(d,"posted_date").toShortDate()}); 
                        }
                    }*/
                    
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                /*_zRow.find("input").prop('required',true);*/
                this.find("[name='cbFilter1']").setCheckEvent("#gridRefuel input[name='cb']");   
                _zRow.find("[name='posted_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true});
                _zRow.find("[name='doc_date']").datepicker({pickTime  : false, autoclose : true, todayHighlight: true});
                _zRow.find("[name='gas_station_id']").dataBind({
                     sqlCode    : "G215" // gas_station_sel
                    ,text   : "gas_station_name"
                    ,value  : "gas_station_id"
                });
                _zRow.find("[name='vehicle_id']").dataBind({
                     sqlCode    : "D231" // dd_vehicle_sel
                    ,text   : "plate_no"
                    ,value  : "vehicle_id"
                });
                _zRow.find("[name='driver_id']").dataBind({
                     sqlCode    : "D227" // drivers_sel
                    ,text   : "full_name"
                    ,value  : "user_id"
                });
                _zRow.find("[name='pao_id']").dataBind({
                     sqlCode    : "P228" // pao_sel
                    ,text   : "full_name"
                    ,value  : "user_id"
                });
            } 
        });
    }
    
    function displayAccidentTransactions(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridAccidents").dataBind({
             sqlCode            : "A221" //accident_transactions_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 235
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"accident_id"   ,type:"hidden"      ,value: svn (d,"accident_id")}) 
                                        + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }    
                    ,{text:"Accident Date"                       ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"accident_date"     ,type:"input"      ,value: svn(d,"accident_date").toShortDate()})
                                 + app.bs({name:"vehicle_id"        ,type:"hidden"     ,value: vehicle_id});
                        }
                    } 
                    ,{text:"Driver"                         ,type:"select"           ,name:"driver_id"                  ,width:150       ,style:"text-align:left"}
                    ,{text:"Pao"                            ,type:"select"           ,name:"pao_id"                     ,width:150       ,style:"text-align:left"}
                    ,{text:"Accident Type"                  ,type:"input"            ,name:"accident_type_id"           ,width:150       ,style:"text-align:left"}
                    ,{text:"Accident Level"                 ,type:"input"            ,name:"accident_level"             ,width:150       ,style:"text-align:left"}
                    ,{text:"Error Type"                     ,type:"input"            ,name:"error_type_id"              ,width:150       ,style:"text-align:left"}
                    ,{text:"Comments"                       ,type:"input"            ,name:"comments"                   ,width:150       ,style:"text-align:left"} 
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                _zRow.find("[name='accident_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true}); 
                _zRow.find("[name='vehicle_id']").dataBind({
                     sqlCode    : "D231" // dd_vehicle_sel
                    ,text   : "plate_no"
                    ,value  : "vehicle_id"
                });
                _zRow.find("[name='driver_id']").dataBind({
                     sqlCode    : "D227" // drivers_sel
                    ,text   : "full_name"
                    ,value  : "user_id"
                });
                _zRow.find("[name='pao_id']").dataBind({
                     sqlCode    : "P228" // pao_sel
                    ,text   : "full_name"
                    ,value  : "user_id"
                });
                 
            } 
        });
    }
    
    function displayPMS(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridPMS").dataBind({
             sqlCode            : "V239" //vehicle_pms_sel
            ,parameters         : {vehicle_id: vehicle_id}
            ,height             : $(window).height() - 241
            ,dataRows           : [
                    {text:cb                                                            ,width:25           ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"pms_id"         ,type:"hidden"      ,value: svn (d,"repair_id")}) 
                                        + app.bs({name:"is_edited"                      ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }    
                    ,{text:"PMS Date"                       ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"pms_date"     ,type:"input"      ,value: svn(d,"pms_date").toShortDate()});
                        }
                    }
                    ,{text:"PMS Type"                                                   ,width:120       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"pms_type_id"       ,type:"select"      ,value: svn(d,"pms_type_id")})
                                 + app.bs({name:"vehicle_id"        ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"ODO Reading"                    ,type:"input"            ,name:"odo_reading"                ,width:100       ,style:"text-align:left"}
                    ,{text:"PM Amount"                      ,type:"input"            ,name:"pm_amount"                  ,width:90        ,style:"text-align:right"}
                    ,{text:"PM Location"                    ,type:"input"            ,name:"pm_location"                ,width:250       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comment"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Status"                         ,type:"select"           ,name:"status_id"                  ,width:150       ,style:"text-align:left"}
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                _zRow.find("[name='pms_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true}); 
                _zRow.find("[name='pms_type_id']").dataBind({
                     sqlCode      : "D235" //dd_pms_type_sel
                    ,text         : "pms_desc"
                    ,value        : "pms_type_id"
                });
                
                _zRow.find("[name='status_id']").dataBind({
                     sqlCode      : "S122" //statuses_sel
                    ,text         : "status_desc"
                    ,value        : "status_code"
                });
                 
            } 
        });
    }
    
    function displayRepairs(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridRepairs").dataBind({
             sqlCode            : "V240" //vehicle_repairs_sel
            ,parameters         : {vehicle_id: vehicle_id}
            ,height             : $(window).height() - 235
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb                                                            ,width:25           ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"repair_id"      ,type:"hidden"      ,value: svn (d,"repair_id")}) 
                                        + app.bs({name:"is_edited"                      ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }    
                    ,{text:"Repair Date"                       ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"repair_date"     ,type:"input"      ,value: svn(d,"repair_date").toShortDate()});
                        }
                    }
                    ,{text:"PMS Type"                                                   ,width:120       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"pms_type_id"       ,type:"select"      ,value: svn(d,"pms_type_id")})
                                 + app.bs({name:"vehicle_id"        ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"ODO Reading"                    ,type:"input"            ,name:"odo_reading"                ,width:100       ,style:"text-align:left"}
                    ,{text:"Repair Amount"                  ,type:"input"            ,name:"repair_amount"              ,width:90        ,style:"text-align:right"}
                    ,{text:"Repair Location"                ,type:"input"            ,name:"repair_location"            ,width:250       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comment"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Status"                         ,type:"select"           ,name:"status_id"                  ,width:150       ,style:"text-align:left"}
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                _zRow.find("[name='repair_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true}); 
                _zRow.find("[name='pms_type_id']").dataBind({
                     sqlCode      : "D235" //dd_pms_type_sel
                    ,text         : "pms_desc"
                    ,value        : "pms_type_id"
                });
                
                _zRow.find("[name='status_id']").dataBind({
                     sqlCode      : "S122" //statuses_sel
                    ,text         : "status_desc"
                    ,value        : "status_code"
                });
                 
            } 
        });
    }
    
    function displaySafetyProblems(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridSafetyProblems").dataBind({
             sqlCode            : "S247" //safety_problems_sel
            ,parameters         : {vehicle_id: vehicle_id}
            ,height             : $(window).height() - 235
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb                                                            ,width:25           ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"safety_report_id"      ,type:"hidden"      ,value: svn (d,"safety_report_id")}) 
                                        + app.bs({name:"is_edited"                      ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }    
                    ,{text:"Safety Report Date"                       ,width:120       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"safety_report_date"     ,type:"input"      ,value: svn(d,"safety_report_date").toShortDate()})
                                 + app.bs({name:"vehicle_id"             ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"Safety"                         ,type:"input"            ,name:"safety_id"                  ,width:100       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comments"                   ,width:150       ,style:"text-align:left"}
                    ,{text:"Reported By"                    ,type:"select"           ,name:"reported_by"                ,width:150       ,style:"text-align:left"}
                    ,{text:"Active?"                        ,type:"yesno"            ,name:"is_active"                  ,width:60        ,style:"text-align:left"     ,defaultValue:"Y"}
                    ,{text:"Closed Date"                    ,width:100               ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"closed_date"     ,type:"input"      ,value: svn(d,"closed_date").toShortDate()});
                        }
                    }
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                _zRow.find("[name='repair_date']").datepicker({pickTime  : false , autoclose : true , todayHighlight: true}); 
                _zRow.find("[name='pms_type_id']").dataBind({
                     sqlCode      : "D235" //dd_pms_type_sel
                    ,text         : "pms_desc"
                    ,value        : "pms_type_id"
                });
                
                _zRow.find("[name='status_id']").dataBind({
                     sqlCode      : "S122" //statuses_sel
                    ,text         : "status_desc"
                    ,value        : "status_code"
                });
                 
            } 
        });
    }
        
    function displayInactiveVehicles(vehicle_maker_id){
         var cb = app.bs({name:"cbFilter2",type:"checkbox"});
         $("#gridInactiveVehicles").dataBind({
    	     sqlCode            : "V232" //vehicles_sel
            ,parameters         : {vehicle_maker_id: vehicle_maker_id,is_active: "N"}
	        ,height             : 360 
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ 
                                    return app.bs({name:"vehicle_id"               ,type:"hidden"      ,value: app.svn(d,"vehicle_id")}) 
                                         + app.bs({name:"is_edited"              ,type:"hidden"      ,value: app.svn(d,"is_edited")})
                                         + (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    } 
                    ,{text:"Plate No"                                                           ,width:240       ,style:"text-align:left"
                        ,onRender: function(d){ 
                            return app.bs({name:"plate_no"                  ,type:"input"       ,value: app.svn(d,"plate_no")})
                                 + app.bs({name:"conduction_no"             ,type:"hidden"      ,value: app.svn(d,"conduction_no")})
                                 + app.bs({name:"chassis_no"                ,type:"hidden"      ,value: app.svn(d,"chassis_no")})
                                 + app.bs({name:"engine_no"                 ,type:"hidden"      ,value: app.svn(d,"engine_no")})
                                 + app.bs({name:"date_acquired"             ,type:"hidden"      ,value: app.svn(d,"date_acquired").toShortDate()})
                                 + app.bs({name:"exp_registration_date"     ,type:"hidden"      ,value: app.svn(d,"exp_registration_date").toShortDate()})
                                 + app.bs({name:"exp_insurance_date"        ,type:"hidden"      ,value: app.svn(d,"exp_insurance_date").toShortDate()})
                                 + app.bs({name:"vehicle_maker_id"          ,type:"hidden"      ,value: app.svn(d,"vehicle_maker_id")})
                                 + app.bs({name:"odometer_reading"          ,type:"hidden"      ,value: app.svn(d,"odometer_reading")});
                
                        }
                    }
                    ,{text:"Active?"                                                            ,width:60        ,style:"text-align:left"     ,defaultValue:"N"
                        ,onRender: function(d){ 
                            return app.bs({name:"is_active"                 ,type:"yesno"       ,value: app.svn(d,"is_active")})
                                 + app.bs({name:"status_id"                 ,type:"hidden"      ,value: app.svn(d,"status_id")})
                                 + app.bs({name:"hash_key"                  ,type:"hidden"      ,value: app.svn(d,"hash_key")})
                                 + app.bs({name:"client_id"                 ,type:"hidden"      ,value: app.userInfo.client_id});
                
                        }
                    }
                ] 
            ,onComplete : function(d){    
                this.find("[name='cbFilter2']").setCheckEvent("#gridInactiveVehicles input[name='cb']");  
            }
        });    
    }
    
    $("#btnInactive").click(function(){
        $(".modal-title").text("Inactive Vehicle(s)");
        $('#modalInactive').modal({ show: true, keyboard: false, backdrop: 'static' });
        displayInactiveVehicles(gVehicleId);
        
    });
    
    $("#btnSaveInactive").click(function(){
       $("#gridInactiveVehicles").jsonSubmit({
                 procedure: "vehicles_upd"
                ,optionalItems: ["is_active","status_id"]
                ,onComplete: function (data) {
                    if(data.isSuccess===true) zsi.form.showAlert("alert");
                    displayInactiveVehicles(gVehicleId);
                    displayVehicles();
                    $('#modalInactive').modal('toggle');
                }
        });
    });
        
    $("#btnSaveVehicleMaker").click(function(){
        $("#gridVehicleMaker").jsonSubmit({
            procedure:"vehicle_maker_upd"
            ,onComplete:function(data){
                if(data.isSuccess===true)zsi.form.showAlert("alert");
                displayVehicleMaker();
            }
        });
    });
    
    $("#btnDeleteVehicleMaker").click(function(){
        zsi.form.deleteData({ 
                code:"ref-00011" 
               ,onComplete:function(data){
                     displayVehicleMaker();
               }
        });
    });
            
    $("#btnSaveVehicle").click(function(){ 
        $("#gridVehicle").jsonSubmit({
             procedure: "vehicles_upd"
            ,optionalItems: ["is_active","status_id"] 
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayVehicles();
            } 
        }); 
    });
    
    $("#btnDeleteVehicles").click(function(){ 
        zsi.form.deleteData({ 
            code:"ref-00012"
           ,onComplete:function(data){
                displayInactiveVehicles(gVehicleId);
                displayVehicles(gVehicleId);
                $('#modalInactive').modal('toggle');
           }
        });
    });
    
    $("#btnSaveRefuel").click(function () { 
        $("#gridRefuel").jsonSubmit({
             procedure: "refuel_transactions_upd"
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayRefuelTransactions(gVehicleId);
            } 
        }); 
    });
    
   $("#btnDeleteRefuel").click(function (){ 
        zsi.form.deleteData({ 
            code:"ref-00014"
           ,onComplete:function(data){
                displayRefuelTransactions(gVehicleId);
           }
        });
    });
    
    $("#btnSaveRepairs").click(function () { 
        $("#gridRepairs").jsonSubmit({
             procedure: "vehicle_repairs_upd"
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayRepairs(gVehicleId);
            } 
        }); 
    });
    
   $("#btnDeleteRepairs").click(function (){ 
        zsi.form.deleteData({ 
            code:"ref-00017"
           ,onComplete:function(data){
                displayRepairs(gVehicleId);
           }
        });
    });
    
    $("#btnSaveAccidents").click(function () { 
        $("#gridAccidents").jsonSubmit({
             procedure: "accident_transactions_upd" 
            ,onComplete: function (data) { 
               if(data.isSuccess===true) zsi.form.showAlert("alert"); 
                displayAccidentTransactions(gVehicleId);
            } 
        }); 
    });
    
   $("#btnDeleteAccidents").click(function (){ 
        zsi.form.deleteData({ 
            code:"ref-00015"
           ,onComplete:function(data){
                displayAccidentTransactions(gVehicleId);
           }
        });
    });
    
    $("#btnFilterAsset").click(function(){ 
        displayVehicles(gVehicleId);
    });
    
    $("#btnSearchVal").click(function(){ 
        var _searchVal = $.trim($("#searchVal").val()); 
        if(gActiveTab === "nav-vehicles") displayVehicleMaker(_searchVal);
        else displayVehicles(gVehicleId,_searchVal);
        
    }); 
   $("#searchVal").on('keypress',function(e){
        var _searchVal = $.trim($("#searchVal").val()); 
        if(e.which == 13) {
           if(gActiveTab === "nav-vehicles") displayVehicleMaker(_searchVal);
           else displayVehicles(gVehicleId,_searchVal);
        }
    });

    $("#searchVal").keyup(function(){
        if($(this).val() === "") {
            if(gActiveTab === "nav-vehicles") displayVehicleMaker();
            else displayVehicles(gVehicleId);
        }
    });
    
    $("#btnResetVal").click(function(){
        $("#searchVal").val("");
        $("#nav-tab").find("[aria-controls='nav-vehicles']").hide();
    });
    
})();        