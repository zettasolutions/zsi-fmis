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
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayRefuelTransactions(gVehicleId);
                    break;
                case "#nav-pms":
                    gActiveTab = "pms";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayPMS(gVehicleId);
                    break;
                case "#nav-accidents":
                    gActiveTab = "accidents";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayAccidentTransactions(gVehicleId);
                    break;
                case "#nav-repairs":
                    gActiveTab = "repairs";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayRepairs(gVehicleId);
                    break;
                case "#nav-safety-problems":
                    gActiveTab = "safety-problems";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displaySafetyProblems(gVehicleId);
                    break;
                case "#nav-parts-replacements":
                    gActiveTab = "parts-replacements";
                    $("#searchVal").val("");
                    $("#vehicleDiv").removeClass("hide");
                    $("#dummyDiv").addClass("hide");
                    $("#vehicleId").val(gVehicleId).trigger('change');
                    //$("#searchDiv").addClass("hide");
                    $("#dummyDivSearch").removeClass("hide");
                    displayPartsReplacements(gVehicleId);
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
	        ,height             : $(window).height() - 271 
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
            	            $("#nav-tab").find("[aria-controls='nav-parts-replacements']").hide();
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
            	            $("#nav-tab").find("[aria-controls='nav-parts-replacements']").show();
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
             sqlCode            : "D260" //dashboard_refuel_transactions_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb        ,width:25              ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"refuel_id"   ,type:"hidden"      ,value: app.svn(d,"refuel_id")}) 
                                        + app.bs({name:"is_edited"                  ,type:"hidden"      ,value: app.svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }   
                    ,{text:"Document No"                         ,type:"input"          ,name:"doc_no"                    ,width:100       ,style:"text-align:left"}
                    ,{text:"Document Date"                       ,width:100       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"doc_date"     ,type:"input"      ,value: app.svn(d,"doc_date").toShortDate()})
                                 + app.bs({name:"vehicle_id"   ,type:"hidden"     ,value: vehicle_id}); 
                        }
                    } 
                    ,{text:"Driver"                         ,type:"input"           ,name:"driver"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Pao"                            ,type:"input"           ,name:"pao"                       ,width:150       ,style:"text-align:left"}
                    ,{text:"Odo Reading"                    ,type:"input"           ,name:"odo_reading"               ,width:100       ,style:"text-align:left"}
                    ,{text:"Gas Station"                    ,type:"input"           ,name:"gas_station_name"          ,width:100       ,style:"text-align:left"}
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
                this.find("input").attr("readonly", true);
            } 
        });
    }
    
    function displayAccidentTransactions(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridAccidents").dataBind({
             sqlCode            : "D258" //dashboard_accident_transactions_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
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
                    ,{text:"Driver"                         ,type:"input"            ,name:"driver"                     ,width:150       ,style:"text-align:left"}
                    ,{text:"Pao"                            ,type:"input"            ,name:"pao"                        ,width:150       ,style:"text-align:left"}
                    ,{text:"Accident Type"                  ,type:"input"            ,name:"accident_type"              ,width:150       ,style:"text-align:left"}
                    ,{text:"Accident Level"                 ,type:"input"            ,name:"accident_level"             ,width:150       ,style:"text-align:left"}
                    ,{text:"Error Type"                     ,type:"input"            ,name:"error_type"                 ,width:150       ,style:"text-align:left"}
                    ,{text:"Comments"                       ,type:"input"            ,name:"comments"                   ,width:150       ,style:"text-align:left"} 
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                this.find("input").attr("readonly", true);
            } 
        });
    }
    
    function displayPMS(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridPMS").dataBind({
             sqlCode            : "D262" //dashboard_vehicle_pms_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
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
                            return app.bs({name:"pms_desc"          ,type:"input"      ,value: svn(d,"pms_desc")})
                                 + app.bs({name:"vehicle_id"        ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"ODO Reading"                    ,type:"input"            ,name:"odo_reading"                ,width:100       ,style:"text-align:left"}
                    ,{text:"PM Amount"                      ,type:"input"            ,name:"pm_amount"                  ,width:90        ,style:"text-align:right"}
                    ,{text:"PM Location"                    ,type:"input"            ,name:"pm_location"                ,width:250       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comment"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Status"                         ,type:"input"            ,name:"status_id"                  ,width:150       ,style:"text-align:left"}
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                this.find("input").attr("readonly", true);
                 
            } 
        });
    }
    
    function displayRepairs(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridRepairs").dataBind({
             sqlCode            : "D263" //dashboard_vehicle_repairs_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
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
                            return app.bs({name:"pms_desc"          ,type:"input"       ,value: svn(d,"pms_desc")})
                                 + app.bs({name:"vehicle_id"        ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"ODO Reading"                    ,type:"input"            ,name:"odo_reading"                ,width:100       ,style:"text-align:left"}
                    ,{text:"Repair Amount"                  ,type:"input"            ,name:"repair_amount"              ,width:90        ,style:"text-align:right"}
                    ,{text:"Repair Location"                ,type:"input"            ,name:"repair_location"            ,width:250       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comment"                    ,width:150       ,style:"text-align:left"}
                    ,{text:"Status"                         ,type:"input"            ,name:"status_id"                  ,width:150       ,style:"text-align:left"}
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                this.find("input").attr("readonly", true);
                 
            } 
        });
    }
    
    function displaySafetyProblems(vehicle_id,searchVal){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridSafetyProblems").dataBind({
             sqlCode            : "D261" //dashboard_safety_problems_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
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
                    ,{text:"Safety"                         ,type:"input"            ,name:"safety_name"                ,width:100       ,style:"text-align:left"}
                    ,{text:"Comment"                        ,type:"input"            ,name:"comments"                   ,width:150       ,style:"text-align:left"}
                    ,{text:"Reported By"                    ,type:"input"            ,name:"reported_by"                ,width:150       ,style:"text-align:left"}
                    //,{text:"Active?"                        ,type:"input"            ,name:"is_active"                  ,width:60        ,style:"text-align:left"     ,defaultValue:"Y"}
                    ,{text:"Closed Date"                    ,width:100               ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"closed_date"     ,type:"input"      ,value: svn(d,"closed_date").toShortDate()});
                        }
                    }
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridAccident input[name='cb']");   
                this.find("input").attr("readonly", true); 
                
                 
            } 
        });
    }
    
    function displayPartsReplacements(vehicle_id){  
        var cb = app.bs({name:"cbFilter1",type:"checkbox"}); 
        $("#gridPartsReplacements").dataBind({
             sqlCode            : "D259" //dashboard_part_replacements_sel
            ,parameters         : {vehicle_id: vehicle_id,search_val:(searchVal ? searchVal : "")}
            ,height             : $(window).height() - 271
            //,blankRowsLimit     : 5
            ,dataRows           : [
                    {text:cb                                                            ,width:25           ,style : "text-align:left"
                        ,onRender  :  function(d){ return app.bs({name:"replacement_id"      ,type:"hidden"      ,value: svn (d,"replacement_id")}) 
                                        + app.bs({name:"is_edited"                      ,type:"hidden"      ,value: svn(d,"is_edited")}) 
                                        +  (d !==null ? app.bs({name:"cb",type:"checkbox"}) : "" );
                                        
                        }
                    
                    }    
                    ,{text:"Replacement Date"                       ,width:120       ,style:"text-align:left"
                        ,onRender   : function(d){ 
                            return app.bs({name:"replacement_date"       ,type:"input"       ,value: svn(d,"replacement_date").toShortDate()})
                                 + app.bs({name:"vehicle_id"             ,type:"hidden"      ,value: vehicle_id});
                        }
                    }
                    ,{text:"Part"                           ,type:"select"           ,name:"part_desc"                  ,width:100       ,style:"text-align:left"}
                    ,{text:"Part Quantity"                  ,type:"input"            ,name:"part_qty"                   ,width:150       ,style:"text-align:left"}
                    ,{text:"Unit"                           ,type:"select"           ,name:"unit_name"                  ,width:150       ,style:"text-align:left"}
                    
                ] 
            ,onComplete : function(d){ 
                var _zRow = this.find(".zRow");
                this.find("[name='cbFilter1']").setCheckEvent("#gridPartsReplacements input[name='cb']");   
                this.find("input").attr("readonly", true); 
                
                 
            } 
        });
    }
        
   
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
    
    $("#btnFilterVehicle").click(function(){ 
        displayRefuelTransactions(gVehicleId);
        displayPMS(gVehicleId);
        displayAccidentTransactions(gVehicleId);
        displayRepairs(gVehicleId);
        displaySafetyProblems(gVehicleId);
        displayPartsReplacements(gVehicleId);
    });
    
    $("#btnSearchVal").click(function(){ 
        var _searchVal = $.trim($("#searchVal").val()); 
        if(gActiveTab === "fuel") displayRefuelTransactions(gVehicleId,_searchVal);
        else if(gActiveTab === "pms") displayPMS(gVehicleId,_searchVal);
        else if(gActiveTab === "accidents") displayAccidentTransactions(gVehicleId,_searchVal);
        else if(gActiveTab === "repairs") displayRepairs(gVehicleId,_searchVal);
        else if(gActiveTab === "safety-problems") displaySafetyProblems(gVehicleId,_searchVal);
        else if(gActiveTab === "parts-replacements") displayPartsReplacements(gVehicleId,_searchVal);
        else displayVehicles(_searchVal);
        
    }); 
   $("#searchVal").on('keypress',function(e){
        var _searchVal = $.trim($("#searchVal").val()); 
        if(e.which == 13) {
            if(gActiveTab === "fuel") displayRefuelTransactions(gVehicleId,_searchVal);
            else if(gActiveTab === "pms") displayPMS(gVehicleId,_searchVal);
            else if(gActiveTab === "accidents") displayAccidentTransactions(gVehicleId,_searchVal);
            else if(gActiveTab === "repairs") displayRepairs(gVehicleId,_searchVal);
            else if(gActiveTab === "safety-problems") displaySafetyProblems(gVehicleId,_searchVal);
            else if(gActiveTab === "parts-replacements") displayPartsReplacements(gVehicleId,_searchVal);
            else displayVehicles(_searchVal);
        }
    });

    $("#searchVal").keyup(function(){
        if($(this).val() === "") {
            if(gActiveTab === "fuel") displayRefuelTransactions(gVehicleId);
            if(gActiveTab === "pms") displayPMS(gVehicleId);
            if(gActiveTab === "accidents") displayAccidentTransactions(gVehicleId);
            if(gActiveTab === "repairs") displayRepairs(gVehicleId);
            if(gActiveTab === "safety-problems") displaySafetyProblems(gVehicleId);
            if(gActiveTab === "parts-replacements") displayPartsReplacements(gVehicleId);
            else displayVehicles();
        }
    });
    
    $("#btnResetVal").click(function(){
        $("#searchVal").val("");
    });
    
})();            