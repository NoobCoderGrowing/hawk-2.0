package hawk.segment.core;

import java.util.HashMap;

public class ChineseUtils {
    private final static String SIMPLE_VS_TRADITIONAL = "碍:礙\t肮:骯\t袄:襖\t坝:壩\t板:闆\t办:辦\t帮:幫\t宝:寶\t报:報\t" +
            "币:幣\t毙:斃\t标:標\t表:錶\t别:彆\t卜:蔔\t补:補\t才:纔\t蚕:蠶\t灿:燦\t层:層\t搀:攙\t谗:讒\t馋:饞\t缠:纏\t" +
            "忏:懺\t偿:償\t厂:廠\t彻:徹\t尘:塵\t衬:襯\t称:稱\t惩:懲\t迟:遲\t冲:衝\t丑:醜\t出:齣\t础:礎\t处:處\t触:觸\t" +
            "辞:辭\t聪:聰\t丛:叢\t担:擔\t胆:膽\t导:導\t灯:燈\t邓:鄧\t敌:敵\t籴:糴\t递:遞\t点:點\t淀:澱\t电:電\t冬:鼕\t" +
            "斗:鬥\t独:獨\t吨:噸\t夺:奪\t堕:墮\t儿:兒\t矾:礬\t范:範\t飞:飛\t坟:墳\t奋:奮\t粪:糞\t凤:鳳\t肤:膚\t妇:婦\t" +
            "复:復,複\t盖:蓋\t干:乾,幹\t赶:趕\t个:個\t巩:鞏\t沟:溝\t构:構\t购:購\t谷:榖\t顾:顧\t刮:颳\t关:關\t观:觀\t" +
            "柜:櫃\t汉:漢\t号:號\t合:閤\t轰:轟\t后:後\t胡:鬍\t壶:壺\t沪:滬\t护:護\t划:劃\t怀:懷\t坏:壞\t欢:歡\t环:環\t" +
            "还:還\t回:迴\t伙:夥\t获:獲,穫\t击:撃\t鸡:鷄\t积:積\t极:極\t际:際\t继:繼\t家:傢\t价:價\t艰:艱\t歼:殲\t茧:繭\t" +
            "拣:揀\t硷:鹸\t舰:艦\t姜:薑\t浆:漿\t桨:槳\t奖:奬\t讲:講\t酱:醤\t胶:膠\t阶:階\t疖:癤\t洁:潔\t借:藉\t仅:僅\t" +
            "惊:驚\t竞:競\t旧:舊\t剧:劇\t据:據\t惧:懼\t卷:捲\t开:開\t克:剋\t垦:墾\t恳:懇\t夸:誇\t块:塊\t亏:虧\t困:睏\t" +
            "腊:臘\t蜡:蠟\t兰:蘭\t拦:攔\t栏:欄\t烂:爛\t累:纍\t垒:壘\t类:類\t里:裏\t礼:禮\t隶:隷\t帘:簾\t联:聯\t怜:憐\t" +
            "炼:煉\t练:練\t粮:糧\t疗:療\t辽:遼\t了:瞭\t猎:獵\t临:臨\t邻:鄰\t岭:嶺\t庐:廬\t芦:蘆\t炉:爐\t陆:陸\t驴:驢\t" +
            "乱:亂\t么:麽\t霉:黴\t蒙:矇,濛,懞\t梦:夢\t面:麵\t庙:廟\t灭:滅\t蔑:衊\t亩:畝\t恼:惱\t脑:腦\t拟:擬\t酿:釀\t" +
            "疟:瘧\t盘:盤\t辟:闢\t苹:蘋\t凭:憑\t扑:撲\t仆:僕\t朴:樸\t启:啓\t签:籤\t千:韆\t牵:牽\t纤:縴,纖\t窍:竅\t窃:竊\t" +
            "寝:寢\t庆:慶\t琼:瓊\t秋:鞦\t曲:麯\t权:權\t劝:勸\t确:確\t让:讓\t扰:擾\t热:熱\t认:認\t洒:灑\t伞:傘\t丧:喪\t" +
            "扫:掃\t涩:澀\t晒:曬\t伤:傷\t舍:捨\t沈:瀋\t声:聲\t胜:勝\t湿:濕\t实:實\t适:適\t势:勢\t兽:獸\t书:書\t术:術\t" +
            "树:樹\t帅:帥\t松:鬆\t苏:蘇,囌\t虽:雖\t随:隨\t台:臺,檯,颱\t态:態\t坛:壇,罎\t叹:嘆\t誊:謄\t体:體\t粜:糶\t铁:鐵\t" +
            "听:聽\t厅:廳\t头:頭\t图:圖\t涂:塗\t团:團,糰\t椭:橢\t洼:窪\t袜:襪\t网:網\t卫:衛\t稳:穩\t务:務\t雾:霧\t牺:犧\t" +
            "习:習\t系:係,繫\t戏:戲\t虾:蝦\t吓:嚇\t咸:鹹\t显:顯\t宪:憲\t县:縣\t响:響\t向:嚮\t协:協\t胁:脅\t亵:褻\t衅:釁\t" +
            "兴:興\t须:鬚\t悬:懸\t选:選\t旋:鏇\t压:壓\t盐:鹽\t阳:陽\t养:養\t痒:癢\t样:様\t钥:鑰\t药:藥\t爷:爺\t叶:葉\t" +
            "医:醫\t亿:億\t忆:憶\t应:應\t痈:癰\t拥:擁\t佣:傭\t踊:踴\t忧:憂\t优:優\t邮:郵\t余:餘\t御:禦\t吁:籲\t郁:鬱\t" +
            "誉:譽\t渊:淵\t园:園\t远:遠\t愿:願\t跃:躍\t运:運\t酝:醖\t杂:雜\t赃:臓\t脏:贜,髒\t凿:鑿\t枣:棗\t灶:竈\t斋:齋\t" +
            "毡:氈\t战:戰\t赵:趙\t折:摺\t这:這\t征:徵\t症:癥\t证:證\t只:隻,祗,衹\t致:緻\t制:製\t钟:鐘,鍾\t肿:腫\t种:種\t" +
            "众:衆\t昼:晝\t朱:硃\t烛:燭\t筑:築\t庄:莊\t桩:樁\t妆:妝\t装:裝\t壮:壯\t状:狀\t准:凖\t浊:濁\t总:總\t钻:鑽\t" +
            "爱:愛\t罢:罷\t备:備\t贝:貝\t笔:筆\t毕:畢\t边:邊\t宾:賓\t参:參\t仓:倉\t产:産\t长:長\t尝:嘗\t车:車\t齿:齒\t" +
            "虫:蟲\t刍:芻\t从:從\t窜:竄\t达:達\t带:帶\t单:單\t当:當,噹\t党:黨\t东:東\t动:動\t断:斷\t对:對\t队:隊\t尔:爾\t" +
            "发:發,髮\t丰:豐\t风:風\t冈:岡\t广:廣\t归:歸\t龟:龜\t国:國\t过:過\t华:華\t画:畫\t汇:匯,彙\t会:會\t几:幾\t" +
            "夹:夾\t戋:戔\t监:監\t见:見\t荐:薦\t将:將\t节:節\t尽:盡,儘\t进:進\t举:舉\t壳:殻\t来:來\t乐:樂\t离:離\t历:歷,曆\t" +
            "丽:麗\t两:兩\t灵:靈\t刘:劉\t龙:龍\t娄:婁\t卢:盧\t虏:虜\t卤:鹵,滷\t录:録\t虑:慮\t仑:侖\t罗:羅\t马:馬\t买:買\t" +
            "卖:賣\t麦:麥\t门:門\t黾:黽\t难:難\t鸟:鳥\t聂:聶\t宁:寜\t农:農\t齐:齊\t岂:豈\t气:氣\t迁:遷\t佥:僉\t乔:喬\t" +
            "亲:親\t穷:窮\t区:區\t啬:嗇\t杀:殺\t审:審\t圣:聖\t师:師\t时:時\t寿:夀\t属:屬\t双:雙\t肃:肅\t岁:嵗\t孙:孫\t" +
            "条:條\t万:萬\t为:為\t韦:韋\t乌:烏\t无:無\t献:獻\t乡:鄉\t写:寫\t寻:尋\t亚:亞\t严:嚴\t厌:厭\t尧:堯\t业:業\t" +
            "页:頁\t义:義\t艺:兿\t阴:陰\t隐:隱\t犹:猶\t鱼:魚\t与:與\t云:雲\t郑:鄭\t执:執\t质:質\t专:專\t嗳:噯\t嫒:嬡\t" +
            "叆:靉\t瑷:璦\t暧:曖\t摆:擺,襬\t罴:羆\t惫:憊\t贞:貞\t则:則\t负:負\t贡:貢\t呗:唄\t员:員\t财:財\t狈:狽\t责:責\t" +
            "厕:厠\t贤:賢\t账:賬\t贩:販\t贬:貶\t败:敗\t贮:貯\t贪:貪\t贫:貧\t侦:偵\t侧:側\t货:貨\t贯:貫\t测:測\t浈:湞\t" +
            "恻:惻\t贰:貳\t贲:賁\t贳:貰\t费:費\t郧:鄖\t勋:勛\t帧:幀\t贴:貼\t贶:貺\t贻:貽\t贱:賤\t贵:貴\t钡:鋇\t贷:貸\t" +
            "贸:貿\t贺:賀\t陨:隕\t涢:溳\t资:資\t祯:禎\t贾:賈\t损:損\t贽:贄\t埙:塤\t桢:楨\t唝:嗊\t唢:嗩\t赅:賅\t圆:圓\t" +
            "贼:賊\t贿:賄\t赆:贐\t赂:賂\t债:債\t赁:賃\t渍:漬\t惯:慣\t琐:瑣\t赉:賚\t匮:匱\t掼:摜\t殒:殞\t勚:勩\t赈:賑\t" +
            "婴:嬰\t啧:嘖\t赊:賒\t帻:幘\t偾:僨\t铡:鍘\t绩:績\t溃:潰\t溅:濺\t赓:賡\t愦:憒\t愤:憤\t蒉:蕢\t赍:賫\t蒇:蕆\t" +
            "赔:賠\t赕:賧\t遗:遺\t赋:賦\t喷:噴\t赌:賭\t赎:贖\t赏:賞\t赐:賜\t赒:賙\t锁:鎖\t馈:饋\t赖:賴\t赪:赬\t碛:磧\t" +
            "㱮:殨\t赗:賵\t腻:膩\t赛:賽\t赘:贅\t撄:攖\t槚:檟\t嘤:嚶\t赚:賺\t赙:賻\t罂:罌\t镄:鐨\t箦:簀\t鲗:鰂\t缨:纓\t" +
            "璎:瓔\t聩:聵\t樱:櫻\t赜:賾\t篑:簣\t濑:瀨\t瘿:癭\t懒:懶\t赝:贋\t豮:豶\t赠:贈\t鹦:鸚\t獭:獺\t赞:贊\t赢:贏\t" +
            "赡:贍\t癞:癩\t攒:攢\t籁:籟\t缵:纘\t瓒:瓚\t臜:臢\t赣:贛\t趱:趲\t躜:躦\t戆:戇\t滗:潷\t荜:蓽\t笾:籩\t傧:儐\t" +
            "殡:殯\t鬓:鬢\t渗:滲\t瘆:瘮\t伧:傖\t抢:搶\t戗:戧\t浐:滻\t伥:倀\t账:賬\t鲿:鱨\t轧:軋\t库:庫\t轫:軔\t斩:斬\t" +
            "轶:軼\t轳:轤\t轸:軫\t莲:蓮\t轿:轎\t琏:璉\t啭:囀\t辋:輞\t暂:暫\t辏:輳\t辔:轡\t辘:轆\t辚:轔\t哔:嗶\t滨:濱\t" +
            "槟:檳\t惨:慘\t碜:磣\t创:創\t呛:嗆\t疮:瘡\t萨:薩\t怅:悵\t胀:脹\t军:軍\t连:連\t轭:軛\t软:軟\t轲:軻\t轴:軸\t" +
            "轺:軺\t较:較\t晕:暈\t辅:輔\t崭:嶄\t辍:輟\t辉:輝\t辐:輻\t辖:轄\t撵:攆\t筚:篳\t摈:擯\t膑:臏\t掺:摻\t沧:滄\t" +
            "炝:熗\t鸧:鶬\t铲:鏟\t帐:帳\t涨:漲\t轨:軌\t轩:軒\t匦:匭\t浑:渾\t轱:軲\t挥:揮\t涟:漣\t轼:軾\t渐:漸\t辄:輒\t" +
            "裤:褲\t辊:輥\t辈:輩\t辑:輯\t辕:轅\t鲢:鰱\t跸:蹕\t嫔:嬪\t镔:鑌\t骖:驂\t糁:糝\t怆:愴\t玱:瑲\t舱:艙\t张:張\t" +
            "厍:厙\t诨:諢\t转:轉\t恽:惲\t轷:軤\t荤:葷\t珲:琿\t轾:輊\t惭:慚\t辆:輛\t裢:褳\t椠:槧\t链:鏈\t输:輸\t辗:輾\t" +
            "辙:轍\t缤:繽\t髌:髕\t毵:毿\t苍:蒼\t枪:槍\t跄:蹌\t枨:棖\t阵:陣\t郓:鄆\t轮:輪\t砗:硨\t轻:輕\t轹:轢\t载:載\t" +
            "辂:輅\t皲:皸\t堑:塹\t辇:輦\t辎:輜\t翚:翬\t毂:轂\t舆:輿\t錾:鏨\t龀:齔\t啮:嚙\t龆:齠\t龅:齙\t龃:齟\t龄:齡\t" +
            "龇:齜\t龈:齦\t龉:齬\t龊:齪\t龌:齷\t龋:齲\t蛊:蠱\t诌:謅\t邹:鄒\t驺:騶\t绉:縐\t皱:皺\t趋:趨\t雏:雛\t苁:蓯\t" +
            "纵:縱\t枞:樅\t怂:慫\t耸:聳\t撺:攛\t㳠:澾\t滞:滯\t郸:鄲\t婵:嬋\t箪:簞\t挡:擋\t谠:讜\t冻:凍\t鸫:鶇\t恸:慟\t" +
            "簖:籪\t怼:懟\t坠:墜\t迩:邇\t泼:潑\t沣:灃\t讽:諷\t飒:颯\t飗:飀\t刚:剛\t钢:鋼\t邝:鄺\t旷:曠\t镩:鑹\t蹿:躥\t" +
            "闼:闥\t挞:撻\t哒:噠\t鞑:韃\t惮:憚\t阐:闡\t掸:撣\t弹:彈\t禅:禪\t殚:殫\t瘅:癉\t蝉:蟬\t蕲:蘄\t冁:囅\t档:檔\t" +
            "裆:襠\t铛:鐺\t傥:儻\t镋:钂\t陈:陳\t岽:崬\t栋:棟\t胨:腖\t弥:彌,瀰\t祢:禰\t玺:壐\t猕:獼\t废:廢\t拨:撥\t艳:艶\t" +
            "滟:灧\t沨:渢\t岚:嵐\t枫:楓\t疯:瘋\t砜:碸\t飓:颶\t飔:颸\t飕:颼\t飘:飄\t飙:飆\t岗:崗\t纲:綱\t\uE82D:棡\t圹:壙\t" +
            "扩:擴\t犷:獷\t纩:纊\t矿:礦\t岿:巋\t阄:鬮\t掴:摑\t帼:幗\t腘:膕\t蝈:蟈\t挝:撾\t哗:嘩\t骅:驊\t烨:燁\t桦:樺\t" +
            "晔:曄\t铧:鏵\t婳:嫿\t㧟:擓\t刽:劊\t哙:噲\t脍:膾\t讥:譏\t矶:磯\t郏:郟\t荚:莢\t铗:鋏\t刬:剗\t栈:棧\t溅:濺\t" +
            "滥:濫\t篮:籃\t苋:莧\t现:現\t觇:覘\t笕:筧\t揽:攬\t觏:覯\t鞯:韉\t蒋:蔣\t栉:櫛\t浕:濜\t琎:璡\t郐:鄶\t狯:獪\t" +
            "鲙:鱠\t叽:嘰\t虮:蟣\t侠:俠\t峡:峽\t颊:頰\t浅:淺\t贱:賤\t践:踐\t蓝:藍\t岘:峴\t枧:梘\t览:覽\t觋:覡\t缆:纜\t" +
            "觐:覲\t锵:鏘\t荩:藎\t侩:儈\t绘:繪\t饥:饑\t陕:陝\t狭:狹\t蛱:蛺\t饯:餞\t盏:盞\t尴:尷\t觃:覎\t觅:覓\t宽:寬\t" +
            "觌:覿\t窥:窺\t觑:覷\t烬:燼\t浍:澮\t烩:燴\t机:機\t浃:浹\t惬:愜\t瘗:瘞\t线:綫\t钱:錢\t槛:檻\t视:視\t觉:覺\t" +
            "蚬:蜆\t靓:靚\t榄:欖\t髋:髖\t赆:贐\t荟:薈\t桧:檜\t玑:璣\t挟:挾\t硖:硤\t箧:篋\t残:殘\t笺:箋\t褴:襤\t规:規\t" +
            "砚:硯\t觊:覬\t搅:攪\t觎:覦\t榉:櫸\t悫:慤\t涞:淶\t莱:萊\t崃:峽\t徕:徠\t赉:賚\t睐:睞\t铼:錸\t泺:濼\t烁:爍\t" +
            "栎:櫟\t轹:轢\t砾:礫\t铄:鑠\t漓:灕\t篱:籬\t沥:瀝\t疬:癧\t俪:儷\t酾:釃\t俩:倆\t颟:顢\t棂:欞\t浏:瀏\t陇:隴\t" +
            "拢:攏\t䶮:龑\t聋:聾\t偻:僂\t喽:嘍\t瘘:瘻\t屦:屨\t泸:濾\t鸬:鸕\t掳:擄\t鹾:鹺\t箓:籙\t滤:濾\t论:論\t纶:綸\t" +
            "萝:蘿\t锣:鑼\t坜:壢\t雳:靂\t郦:酈\t鲡:鱺\t唡:啢\t螨:蟎\t泷:瀧\t茏:蘢\t昽:曨\t龚:龔\t溇:漊\t缕:縷\t褛:褸\t" +
            "蝼:螻\t垆:壚\t颅:顱\t摅:攄\t伦:倫\t轮:輪\t啰:囉\t箩:籮\t苈:藶\t逦:邐\t辆:輛\t魉:魎\t宠:寵\t咙:嚨\t胧:朧\t" +
            "龛:龕\t蒌:蔞\t屡:屢\t窭:窶\t篓:簍\t栌:櫨\t舻:艫\t沦:淪\t瘪:癟\t逻:邏\t呖:嚦\t骊:驪\t满:滿\t懑:懣\t庞:龐\t" +
            "珑:瓏\t砻:礱\t笼:籠\t搂:摟\t数:數\t䁖:瞜\t耧:耬\t轳:轤\t鲈:鱸\t抡:掄\t猡:玀\t枥:櫪\t鹂:鸝\t瞒:瞞\t蹒:蹣\t" +
            "垄:壟\t栊:櫳\t袭:襲\t嵝:嶁\t楼:樓\t镂:鏤\t薮:藪\t擞:擻\t胪:臚\t囵:圇\t椤:欏\t髅:髏\t冯:馮\t驭:馭\t闯:闖\t" +
            "吗:嗎\t犸:獁\t驮:馱\t驰:馳\t驯:馴\t妈:媽\t玛:瑪\t驱:驅\t驳:駁\t码:碼\t驼:駝\t驻:駐\t驵:駔\t驾:駕\t驿:驛\t" +
            "驷:駟\t驶:駛\t驹:駒\t驺:騶\t骀:駘\t驸:駙\t驽:駑\t骂:駡\t蚂:螞\t笃:篤\t骇:駭\t骈:駢\t骁:驍\t骄:驕\t骅:驊\t" +
            "骆:駱\t骊:驪\t骋:騁\t验:驗\t骏:駿\t骎:駸\t骑:騎\t骐:騏\t骒:騍\t骓:騅\t骖:驂\t骗:騙\t骘:騭\t骛:騖\t骚:騷\t" +
            "骞:騫\t骜:驁\t蓦:驀\t骢:驄\t骧:驤\t荬:蕒\t读:讀\t赎:贖\t唛:嘜\t闩:閂\t问:問\t闰:閏\t钔:鍆\t闽:閩\t阀:閥\t" +
            "阅:閲\t阏:閼\t阌:閿\t阑:闌\t阕:闋\t阙:闕\t谰:讕\t渑:澠\t傩:儺\t凫:鳬\t鸣:鳴\t鸥:鷗\t鸪:鴣\t鸯:鴦\t鸵:鴕\t" +
            "䴔:鵁\t鸼:鵃\t鹈:鵜\t鹆:鵒\t腾:騰\t骝:騮\t骟:騸\t骠:驃\t骡:騾\t羁:覊\t骤:驟\t骥:驥\t渎:瀆\t续:續\t椟:櫝\t" +
            "觌:覿\t犊:犢\t牍:牘\t窦:竇\t黩:黷\t麸:麩\t闪:閃\t们:們\t闭:閉\t闯:闖\t扪:捫\t闱:闈\t闵:閔\t闷:悶\t闲:閑\t" +
            "间:間\t闹:閙\t闸:閘\t阂:閡\t闺:閨\t闻:聞\t闼:闥\t闾:閭\t闿:闓\t阁:閣\t润:潤\t涧:澗\t悯:憫\t阆:閬\t阃:閫\t" +
            "阄:鬮\t娴:嫻\t阈:閾\t阉:閹\t阊:閶\t阍:閽\t阋:鬩\t阐:闡\t阎:閻\t焖:燜\t裥:襇\t阔:闊\t痫:癇\t鹇:鷳\t阒:闃\t" +
            "搁:擱\t锏:鐧\t锎:鐦\t阖:闔\t阗:闐\t榈:櫚\t简:簡\t阚:闞\t蔺:藺\t澜:瀾\t斓:斕\t镧:鑭\t躏:躪\t绳:繩\t鼋:黿\t" +
            "蝇:蠅\t鼍:鼉\t滩:灘\t摊:擹\t瘫:癱\t鸠:鳩\t岛:島\t茑:蔦\t鸢:鳶\t枭:梟\t鸩:鴆\t鸦:鴉\t䴓:鳾\t鸨:鴇\t鸧:鶬\t" +
            "窎:窵\t莺:鶯\t捣:搗\t鸫:鶇\t鸬:鸕\t鸭:鴨\t鸮:鴞\t鸲:鴝\t鸰:鴒\t鸳:鴛\t袅:裊\t鸱:鴟\t鸶:鷥\t鸾:鵉\t鸿:鴻\t" +
            "鸷:鷙\t鸸:鴯\t䴕:鴷\t鸽:鴿\t鸹:鴰\t鸺:鵂\t鸻:鴴\t鹇:鷳\t鹁:鵓\t鹂:鸝\t鹃:鵑\t鹄:鵠\t鹅:鵝\t鹑:鶉\t鹒:鶊\t" +
            "䴖:鶄\t鹉:鵡\t鹊:鵲\t鹋:鶓\t鹌:鵪\t鹏:鵬\t鹐:鵮\t鹚:鷀\t鹕:鶘\t鹖:鶡\t䴗:鶪\t鹗:鶚\t鹘:鶻\t鹙:鶖\t鹜:鶩\t" +
            "鹛:鶥\t鹤:鶴\t鹣:鶼\t鹞:鷂\t鹡:鶺\t鷉:鷉\t鹧:鷓\t鹥:鷖\t鹦:鸚\t鹨:鷚\t鹫:鹫\t鹩:鷯\t鹪:鷦\t鹬:鷸\t鹰:鷹\t" +
            "鹯:鸇\t鹭:鷺\t鸊:鸊\t鹳:鸛\t慑:攝\t滠:灄\t摄:攝\t嗫:囁\t镊:鑷\t颞:顳\t蹑:躡\t泞:濘\t聍:聹\t侬:儂\t剂:劑\t" +
            "脐:臍\t齑:齏\t剀:剴\t桤:榿\t忾:愾\t跹:躚\t剑:劍\t验:驗\t裣:襝\t侨:僑\t娇:嶠\t鞒:鞽\t榇:櫬\t藭:藭\t讴:謳\t" +
            "奁:奩\t枢:樞\t眍:瞘\t蔷:薔\t铩:鎩\t谉:讅\t柽:檉\t拧:擰\t浓:濃\t侪:儕\t蛴:蠐\t凯:凱\t觊:覬\t饩:餼\t俭:儉\t" +
            "检:檢\t睑:瞼\t挢:撟\t桥:橋\t伛:傴\t呕:嘔\t瓯:甌\t躯:軀\t墙:墻\t婶:嬸\t蛏:蟶\t咛:嚀\t哝:噥\t济:濟\t跻:蠐\t" +
            "恺:愷\t硙:磑\t险:險\t殓:殮\t签:簽\t荞:蕎\t轿:轎\t沤:漚\t岖:嶇\t欧:歐\t嫱:嬙\t狞:獰\t脓:膿\t荠:薺\t霁:霽\t" +
            "闿:闓\t皑:皚\t捡:撿\t敛:斂\t潋:瀲\t峤:嶠\t硚:礄\t怄:慪\t妪:嫗\t殴:毆\t樯:檣\t柠:檸\t挤:擠\t鲚:鱭\t垲:塏\t" +
            "铠:鎧\t猃:獫\t脸:臉\t蔹:蘞\t骄:驕\t矫:矯\t抠:摳\t驱:驅\t鸥:鷗\t穑:穡\t浉:溮\t狮:獅\t蛳:螄\t筛:篩\t埘:塒\t" +
            "莳:蒔\t鲥:鰣\t俦:儔\t涛:濤\t祷:禱\t焘:燾\t畴:疇\t铸:鑄\t筹:籌\t踌:躊\t嘱:囑\t瞩:矚\t萧:蕭\t啸:嘯\t潇:瀟\t" +
            "箫:簫\t刿:劌\t哕:噦\t秽:穢\t荪:蓀\t狲:猻\t逊:遜\t涤:滌\t绦:縧\t鲦:鰷\t厉:厲\t迈:邁\t励:勵\t疠:癘\t趸:躉\t" +
            "砺:礪\t粝:糲\t蛎:蠣\t伪:僞\t沩:溈\t妫:媯\t讳:諱\t伟:偉\t闱:闈\t违:違\t韧:韌\t帏:幃\t围:圍\t纬:緯\t祎:禕\t" +
            "玮:瑋\t韨:韍\t涠:潿\t韫:韞\t韪:韙\t韬:韜\t邬:鄔\t坞:塢\t呜:嗚\t钨:鎢\t怃:憮\t庑:廡\t抚:撫\t芜:蕪\t妩:嫵\t" +
            "谳:讞\t芗:薌\t飨:饗\t泻:瀉\t浔:潯\t荨:蕁\t挦:撏\t鲟:鱘\t垩:堊\t垭:埡\t挜:掗\t哑:啞\t恶:惡,噁\t氩:氬\t壶:壺\t" +
            "俨:儼\t酽:釅\t恹:懨\t厣:厴\t靥:靨\t餍:饜\t黡:黶\t蟏:蠨\t虿:蠆\t苇:葦\t炜:煒\t韩:韓\t呒:嘸\t娅:婭\t魇:魘\t" +
            "侥:僥\t浇:澆\t挠:撓\t荛:蕘\t峣:嶢\t哓:嘵\t娆:嬈\t骁:驍\t绕:繞\t饶:饒\t烧:焼\t桡:橈\t晓:曉\t硗:磽\t铙:鐃\t" +
            "翘:翹\t蛲:蟯\t跷:蹺\t邺:鄴\t顶:頂\t顷:頃\t项:項\t顸:頇\t顺:順\t须:須\t颃:頏\t烦:煩\t顼:瑣\t顽:頑\t顿:頓\t" +
            "颀:頎\t颁:頒\t颂:頌\t倾:傾\t预:預\t庼:廎\t硕:碩\t颅:顱\t领:領\t颈:頸\t颍:潁\t蓣:蕷\t颗:顆\t颙:顒\t颟:顢\t" +
            "颤:顫\t颦:顰\t议:議\t呓:囈\t荫:蔭\t瘾:癮\t莸:蕕\t鱽:魛\t鲎:鱟\t鲈:鱸\t鲋:鮒\t鲚:鱭\t鲟:鱘\t鲙:鱠\t鲢:鰱\t" +
            "鲤:鯉\t鲸:鯨\t鲻:鯔\t鲶:鯰\t鳊:鯿\t镥:鑥\t鳏:鰥\t鳛:鰼\t鳗:鰻\t鳣:鱣\t屿:嶼\t颇:頗\t颌:頜\t频:頻\t额:額\t" +
            "颛:顓\t颞:顳\t巅:巔\t颧:顴\t仪:儀\t渔:漁\t蓟:薊\t鲇:點\t鲍:鮑\t鲛:鮫\t鲔:鮪\t鲨:鯊\t鲫:鯽\t鲦:鰷\t鲭:鯖\t" +
            "鲳:鯧\t藓:蘚\t鲽:鰈\t鳅:鰍\t鳑:鰟\t鳕:鱈\t鳝:鱔\t鳢:鱧\t欤:歟\t颏:頦\t颋:頲\t颓:頽\t颜:顔\t缬:纈\t颡:顙\t" +
            "颥:顬\t蚁:蟻\t鲂:魴\t鲆:鮃\t鲊:鮓\t鲐:鮐\t鲜:鮮\t鲟:鱘\t噜:嚕\t鲥:鰣\t鲧:鯀\t鲮:鯪\t鲱:鯡\t鳁:鰛\t鳆:鰒\t" +
            "鳒:鰜\t癣:癬\t鳔:鰾\t鳟:鱒\t颊:頰\t滪:澦\t颔:頷\t撷:擷\t濒:瀕\t嚣:囂\t癫:癲\t鱿:魷\t鲏:鮍\t鲞:鯗\t鲑:鮭\t" +
            "鲗:鰂\t鲡:鱺\t鲩:鯇\t橹:櫓\t鲰:鯫\t鲵:鯢\t鳃:鰓\t鳇:鰉\t鳍:鰭\t鳖:鱉\t鳓:鰳\t鳞:鱗\t颉:頡\t颐:頤\t颖:穎\t" +
            "题:題\t颠:顛\t颢:顥\t灏:灝\t鲁:魯\t鲅:鮁\t稣:穌\t鲝:鮺\t鲒:鮚\t鲖:鮦\t鲠:鯁\t鲣:鰹\t氇:氌\t鲲:鯤\t鲷:鯛\t" +
            "鲿:鱨\t鳄:鰐\t鳌:鰲\t鳎:鰨\t鳙:鱅\t鳘:鰵\t鳜:鱖\t芸:蕓\t昙:曇\t叆:靉\t叇:靆\t掷:擲\t踯:躑\t垫:墊\t挚:摯\t" +
            "贽:贄\t鸷:鷙\t蛰:蟄\t絷:縶\t锧:鑕\t踬:躓\t传:傳\t抟:摶\t转:轉\t砖:磚\t啭:囀\t计:計\t讨:討\t讪:訕\t讳:諱\t" +
            "设:設\t论:論\t评:評\t诇:詗\t诉:訴\t该:該\t诘:詰\t诤:諍\t诣:詣\t诞:誕\t诬:誣\t诰:誥\t狱:獄\t谉:讅\t读:讀\t" +
            "诿:諉\t谂:諗\t谝:諞\t谍:諜\t谒:謁\t谥:謚\t谠:讜\t谪:謫\t谱:譜\t谯:譙\t谳:讞\t霭:靄\t饥:饑\t饮:飲\t饲:飼\t" +
            "饳:飿\t饼:餠\t饽:餑\t订:訂\t讧:訌\t训:訓\t讵:詎\t讽:諷\t讼:訟\t诏:詔\t诅:詛\t诈:詐\t详:詳\t诙:詼\t诠:詮\t" +
            "话:話\t浒:滸\t语:語\t诳:誑\t谊:誼\t谇:誶\t诼:諑\t谁:誰\t谛:諦\t谘:諮\t谐:諧\t谔:諤\t谤:謗\t谡:謖\t谫:譾\t" +
            "谮:譖\t蔼:藹\t辩:辯\t饦:飥\t饫:飫\t饯:餞\t饸:餄\t饵:餌\t馁:餒\t讣:訃\t讦:訐\t讫:訖\t讴:謳\t讹:訛\t讻:訩\t" +
            "词:詞\t识:識\t诊:診\t诧:詫\t试:試\t诛:誅\t诡:詭\t诮:誚\t诵:誦\t诱:誘\t谅:諒\t请:請\t诹:諏\t谀:諛\t谙:諳\t" +
            "谌:諶\t谏:諫\t谓:謂\t谦:謙\t谢:謝\t谨:謹\t谭:譚\t槠:櫧\t饧:餳\t饩:餼\t饰:飾\t饷:餉\t饶:饒\t饿:餓\t讥:譏\t" +
            "议:議\t记:記\t讯:訊\t访:訪\t讶:訝\t诀:訣\t讷:訥\t䜣:訢\t许:許\t诂:詁\t诃:訶\t译:譯\t诎:詘\t诌:謅\t诋:詆\t" +
            "诒:詒\t诨:諢\t诓:誆\t诖:詿\t诗:詩\t诩:詡\t诔:誄\t诟:詬\t询:詢\t诚:誠\t说:説\t诫:誡\t罚:罸\t误:誤\t诲:誨\t" +
            "诶:誒\t谈:談\t谆:諄\t诺:諾\t诸:諸\t课:課\t诽:誹\t调:調\t谄:諂\t谜:謎\t谚:諺\t谎:謊\t谋:謀\t谞:諝\t谑:謔\t" +
            "谖:諼\t谕:諭\t谧:謐\t谟:謨\t谣:謡\t储:儲\t谬:謬\t谩:謾\t谰:讕\t谲:譎\t谴:譴\t谵:譫\t雠:讎\t谶:讖\t饨:飩\t" +
            "饭:飯\t饪:飪\t饬:飭\t饱:飽\t饴:飴\t饺:餃\t饻:餏\t蚀:蝕\t饹:餎\t馆:館\t馄:餛\t馃:餜\t馅:餡\t馉:餶\t馇:餷\t" +
            "馈:饋\t馊:餿\t馐:饈\t馍:饃\t馎:餺\t馏:餾\t馑:饉\t馒:饅\t馓:饊\t馔:饌\t馕:饟\t汤:湯\t扬:揚\t场:場\t旸:暘\t" +
            "饧:餳\t炀:煬\t杨:楊\t肠:腸\t疡:瘍\t砀:碭\t畅:暢\t钖:錫\t殇:殤\t荡:蕩\t烫:燙\t觞:觴\t丝:絲\t纠:糾\t纩:纊\t" +
            "纡:紆\t纣:紂\t红:紅\t纪:紀\t纫:紉\t纥:紇\t约:约\t纨:紈\t纭:紜\t纲:綱\t纸:紙\t绊:絆\t绋:紼\t细:細\t终:終\t" +
            "荮:葤\t绕:繞\t绘:繪\t绑:綁\t绢:絹\t综:綜\t绫:綾\t绿:緑\t绶:綬\t绵:綿\t缃:緗\t缇:緹\t缆:纜\t缎:緞\t缣:縑\t" +
            "缜:縝\t缥:縹\t缧:縲\t缭:繚\t缱:繾\t坚:堅\t紧:緊\t劳:勞\t荥:滎\t捞:撈\t萦:縈\t蝾:蠑\t览:覧\t级:級\t纯:純\t" +
            "纱:紗\t纵:縱\t线:綫\t绎:繹\t䌷:紬\t绉:縐\t荭:葒\t绔:絝\t绝:絶\t莼:蒓\t绣:綉\t绽:綻\t绪:緒\t绰:綽\t绸:綢\t" +
            "缁:緇\t缂:緙\t缈:緲\t缓:緩\t辔:轡\t缢:縊\t缝:縫\t缪:繆\t蕴:藴\t橼:櫞\t缴:繳\t贤:賢\t铿:鏗\t茕:煢\t荦:熒\t" +
            "唠:嘮\t痨:癆\t揽:攬\t纺:紡\t纰:紕\t纴:紝\t纾:紓\t绀:紺\t经:經\t绅:紳\t绐:紿\t绞:絞\t结:結\t绛:絳\t绠:綆\t" +
            "绥:綏\t绾:綰\t续:續\t绲:緄\t绷:綳\t缔:締\t缅:緬\t缗:緡\t缄:緘\t缞:縗\t缚:縛\t缡:縭\t缦:縵\t缮:繕\t缰:繮\t" +
            "辫:辮\t肾:腎\t鲣:鰹\t茔:塋\t涝:澇\t莺:鶯\t嵘:嶸\t缆:纜\t纹:紋\t纽:紐\t纷:紛\t纼:紖\t绁:紲\t绍:紹\t织:織\t" +
            "哟:喲\t统:統\t绗:絎\t络:絡\t绨:綈\t绦:縧\t绻:綣\t绮:綺\t绳:繩\t绺:綹\t编:編\t缘:緣\t缊:緼\t缑:緱\t缤:繽\t" +
            "缙:縉\t潍:濰\t缨:纓\t缯:繒\t缳:繯\t缵:纘\t竖:竪\t荧:熒\t崂:嶗\t萤:螢\t铹:鐒\t榄:欖\t纬:緯\t纳:納\t纶:綸\t" +
            "咝:噝\t绂:紱\t组:組\t绌:絀\t绖:絰\t绒:絨\t给:給\t绚:絢\t绡:綃\t鸶:鷥\t绩:績\t缀:綴\t绯:緋\t维:維\t缕:縷\t" +
            "缉:緝\t缌:緦\t缒:縋\t缟:縞\t缛:縟\t缩:縮\t缫:繅\t缬:纈\t缲:繰\t悭:慳\t荣:榮\t莹:瑩\t营:營\t耢:耮\t鉴:鑒\t" +
            "识:識\t帜:幟\t织:織\t炽:熾\t职:職\t钆:釓\t钇:釔\t钌:釕\t钋:釙\t钉:釘\t针:針\t钊:釗\t钗:釵\t钎:釺\t钓:釣\t" +
            "钏:釧\t钍:釷\t钐:釤\t钒:釩\t钖:鍚\t钕:釹\t钔:鍆\t钬:鈥\t钫:鈁\t钚:鈈\t钪:鈧\t钯:鈀\t钭:鈄\t钙:鈣\t钝:鈍\t" +
            "钛:鈦\t钘:鈃\t钮:鈕\t钞:鈔\t钢:鋼\t钠:鈉\t钡:鋇\t钤:鈐\t钧:鈞\t钩:鈎\t钦:欽\t钨:鎢\t铋:鉍\t钰:鈺\t钱:錢\t" +
            "钲:鉦\t钳:鉗\t钴:鈷\t钺:鉞\t钵:鉢\t钿:鈿\t铂:鉑\t铊:鉈\t铉:鉉\t铷:銣\t铫:銚\t铳:銃\t铏:鉶\t铝:鋁\t铣:銑\t" +
            "铧:鏵\t锑:銻\t锓:鋟\t销:銷\t锈:銹\t锕:錒\t锂:鋰\t锗:鍺\t锛:錛\t锡:錫\t锨:鍁\t镁:鎂\t锶:鍶\t镅:鎇\t镎:鎿\t" +
            "镇:鎮\t镝:鏑\t镗:鏜\t镢:鐝\t镱:鐿\t镲:鑔\t峃:嶨\t鲎:鱟\t钹:鈸\t铎:鐸\t铄:鑠\t钽:鉭\t铒:鉺\t铯:銫\t铵:銨\t" +
            "铱:銥\t铙:鐃\t铡:鍘\t铤:鋌\t铩:鎩\t锒:鋃\t锃:鋥\t锁:鎖\t锋:鋒\t锎:鐦\t锧:鑕\t锝:鍀\t锯:鋸\t锣:鑼\t锱:錙\t" +
            "镂:鏤\t锴:鍇\t镄:鐨\t镓:鎵\t镑:鎊\t镍:鎳\t镛:鏞\t镨:鐠\t镣:鐐\t镭:鐳\t镳:鑣\t学:學\t黉:黌\t钼:鉬\t铆:鉚\t" +
            "铌:鈮\t铑:銠\t铥:銩\t衔:銜\t铓:鋩\t银:銀\t铠:鎧\t铭:銘\t揿:撳\t铺:鋪\t链:鏈\t锄:鋤\t锆:鋯\t铽:鋱\t锘:鍩\t" +
            "锫:錇\t锰:錳\t锤:錘\t键:鍵\t锲:鍥\t锾:鍰\t锻:鍛\t镋:钂\t镐:鎬\t镌:鎸\t镞:鏃\t镘:鏝\t镧:鑭\t镫:鐙\t镬:鑊\t" +
            "镴:鑞\t觉:覺\t钾:鉀\t铃:鈴\t铍:鈹\t钷:鉕\t铕:銪\t铪:鉿\t铲:鏟\t铗:鋏\t铛:鐺\t铨:銓\t铬:鉻\t锌:鋅\t铸:鑄\t" +
            "铿:鏗\t锅:鍋\t铹:鐒\t铼:錸\t锞:錁\t错:錯\t锢:錮\t锥:錐\t镀:鍍\t锵:鏘\t锹:鍬\t锸:鍤\t镔:鑌\t镉:鎘\t镏:鎦\t" +
            "镖:鏢\t镩:鑹\t镥:鑥\t镪:鏹\t镮:鐶\t镶:鑲\t搅:攪\t铀:鈾\t铅:鉛\t钶:鈳\t铈:鈰\t铟:銦\t铞:銱\t铰:鉸\t铐:銬\t" +
            "铜:銅\t铢:銖\t铮:錚\t锐:鋭\t嵚:嶔\t锏:鐧\t锉:銼\t锔:鋦\t锇:鋨\t锭:錠\t锚:錨\t锟:錕\t锦:錦\t镃:鎡\t锷:鍔\t" +
            "锿:鎄\t锼:鎪\t镒:鎰\t镊:鑷\t镜:鏡\t镚:鏰\t镦:鐓\t镤:鏷\t镰:鐮\t镯:鐲\t喾:嚳\t译:譯\t泽:澤\t怿:懌\t择:擇\t" +
            "峄:嶧\t绎:繹\t驿:驛\t铎:鐸\t萚:蘀\t释:釋\t箨:籜\t劲:勁\t刭:剄\t陉:陘\t泾:涇\t茎:莖\t径:徑\t经:經\t烃:烴\t" +
            "轻:輕\t氢:氫\t胫:脛\t痉:痙\t羟:羥\t颈:頸\t巯:巰\t变:變\t弯:彎\t孪:孿\t峦:巒\t娈:孌\t恋:戀\t栾:欒\t挛:攣\t" +
            "鸾:鸞\t湾:灣\t蛮:蠻\t脔:臠\t滦:灤\t銮:鑾\t剐:剮\t涡:渦\t埚:堝\t㖞:喎\t莴:萵\t娲:媧\t祸:禍\t脶:腡\t窝:窩\t" +
            "锅:鍋\t蜗:蝸\t宝:寳\t";

    private final static HashMap<Character, Character> TRADITIONAL_TO_SIMPLE_MAP = new HashMap<Character, Character>();

    static {
        String[] pairs = SIMPLE_VS_TRADITIONAL.split("\t");
        for (String pair : pairs){
            String[] words = pair.split(":");
            Character simple = words[0].charAt(0);
            String[] traditionals = words[1].split(",");
            for (String traditional : traditionals) {
                TRADITIONAL_TO_SIMPLE_MAP.put(traditional.charAt(0), simple);
            }
        }
    }

    public static String toSimpleField(String str){
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            Character simple = TRADITIONAL_TO_SIMPLE_MAP.get(charArray[i]);
            if(simple != null){
                charArray[i] = simple;
            }
        }
        return new String(charArray);
    }
}
