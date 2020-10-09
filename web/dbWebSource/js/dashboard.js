 var db=(function(){ 
        $(".page-title").html("Dashboard");    
        $('[data-toggle="tooltip"]').tooltip();       
           
        function displayMonthlyTransactionsChart(){  
            am4core.useTheme(am4themes_animated);
            // Enable queuing 
            am4core.options.queue = true;
            am4core.options.onlyShowOnViewport = true;     
            var _colors = [];
            var _colorRows = [];
            
            var _getData = function(sqlCode,params,cb){   
                zsi.getData({
                     sqlCode : sqlCode 
                    ,parameters : params
                    ,onComplete : function(d) {
                        var _rows = d.rows;  
                        cb(_rows);  
                    }
                });
            }; 
            
            var _getColor = function(cb){ 
                var _colorSet = new am4core.ColorSet();
                    _colors = _colorSet;  
                    cb(_colors);
            };
            
            var _displayBarGraph = function(o){  
                var _dataLength = o.data.length;
                var _data = [];
                var _colorSet = new am4core.ColorSet(); 
                var _getCategoryColor = function(category, index){
                    var _color = $.grep(_colorRows, function(z) {
                        return z.field_name.toUpperCase() == category.toUpperCase();
                    });
                    return (!isUD(_color[0])) ? _color[0].color_value.toLowerCase() : _colorSet.getIndex(index);
                };
                
                var _setData = function(){ 
                    $.each(o.data, function(i,v){  
                        var _json = {};  
                        if(o.title==="Refuel"){
                            _json.category = v.refuel_month;
                            _json.value = v.total_refuel;
                            _json.color = _colorSet.next();  
                        }else if(o.title==="PMS"){
                            _json.category = v.pms_month;
                            _json.value = v.total_pms;
                            _json.color = _colorSet.next();  
                        }else if(o.title==="Repair"){
                            _json.category = v.repair_month;
                            _json.value = v.total_repair;
                            _json.color = _colorSet.next();  
                        }else{
                            _json.category = v.accident_month;
                            _json.value = v.total_accident;
                            _json.color = _colorSet.next();  
                        } 
                        _data.push(_json); 
                    });
                
                };
                
                _setData();  
                am4core.ready(function() { 
                    var chart = am4core.create(o.container, am4charts.XYChart); 
                    chart.numberFormatter.numberFormat = '###';
                    // Set data 
                    var _platform = ""; 
                    var selected;
                    var generateChartData = function() { 
                        var chartData = []; 
                        var _monthlyCtgry = ""; 
                        var _month = []; 
                            _month[1] = "January";
                            _month[2] = "February";
                            _month[3] = "March";
                            _month[4] = "April";
                            _month[5] = "May";
                            _month[6] = "June";
                            _month[7] = "July";
                            _month[8] = "August";
                            _month[9] = "September";
                            _month[10] = "October";
                            _month[11] = "November";
                            _month[12] = "December"; 
                        for (var i = 0; i < _data.length; i++) {      
                            for(var n =0;n<=_month.length; n++){ 
                                if(_data[i].category === n){ 
                                  _monthlyCtgry = _month[n]; 
                                } 
                            }  
                            chartData.push({
                                title       : o.title,  
                                category    : _monthlyCtgry, 
                                container   : o.container, 
                                value       : _data[i].value,
                                color       : _data[i].color,
                                id: i
                            });  
                        }  
                        return chartData; 
                    };  
                    chart.data = generateChartData();  
                   // Add and configure Series     
                    var categoryAxis = chart.xAxes.push(new am4charts.CategoryAxis());  
                        categoryAxis.dataFields.category = "category"; 
                        categoryAxis.renderer.minGridDistance = 20;
                        
                    var valueAxis = chart.yAxes.push(new am4charts.ValueAxis());  
                    var series = chart.series.push(new am4charts.ColumnSeries());  
                        series.dataFields.categoryX = "category"; 
                        series.dataFields.valueY = "value";    
                        series.columns.template.tooltipText = "{categoryX}: [bold]{valueY}[/]";  
                        
                        series.columns.template.tooltipText = "Month of "+"{categoryX}: [bold]{valueY}[/]";  
                        
                        chart.cursor = new am4charts.XYCursor();   
                        // as by default columns of the same series are of the same color, we add adapter which takes colors from chart.colors color set
                        series.columns.template.adapter.add("fill", function (fill, target) { 
                        	return chart.colors.getIndex(target.dataItem.index);
                        });
                
                    var title = chart.titles.create();    
                        if(o.data.length===0)title.text = 'No Monthly Data ';
                        else title.text = 'Monthly Data ';
                        
                });
            };  
            var _init = function(name){  
                var _params = {
                        client_id:app.userInfo.company_id  
                    }
                    ,_sqlCode = "R287"
                    ,_container = "graph1"
                    ,_value = "total_refuel"
                    ,_category = "refuel_month"
                    ,_isColorSet = false
                    ,_name = name
                    ,_json = {}; 
                    
                    switch(name){
                        case "PMS":  
                            _container = "graph2";
                            _sqlCode = "P286"; 
                            _value = "total_pms";
                            _category = "pms_month";
                            _params = {
                                client_id:app.userInfo.company_id 
                            };
                        break;
                        case "Repair":  
                            _container = "graph3";
                            _sqlCode = "R288"; 
                            _value = "total_repair";
                            _category = "repair_month";
                            _params = {
                                client_id:app.userInfo.company_id 
                            }; 
                        break;
                        case "Accidents":  
                            _container = "graph4";
                            _sqlCode = "A285"; 
                            _value = "total_accident";
                            _category = "accident_month";
                            _params = {
                                client_id:app.userInfo.company_id 
                            }; 
                        break;  
                    }
                    
                    _json.title = _name;
                    _json.container = _container;
                    _json.value = _value;
                    _json.category = _category;
                    _json.isColorSet = _isColorSet;  
                    _getColor(function(colorSet){
                        _getData(_sqlCode,_params, function(data){ 
                            _json.colorSet = colorSet;
                            _json.data = data;
                            _json.category = _category; 
                            _json.title = _name;  
                            _displayBarGraph(_json); 
                        });
                    }); 
            }; 
            
            _init("Refuel");
            _init("PMS");
            _init("Repair");
            _init("Accidents");
            
        } displayMonthlyTransactionsChart();
        
     
})();           
           
                                                                                                               
      