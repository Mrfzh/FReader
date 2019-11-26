package com.feng.freader.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/11/6
 */
public class Constant {
    // 男生热门榜单的榜单数
    public static final int MALE_HOT_RANK_NUM = 5;
    // 男生热门榜单的 id
    private static String sZYHotRankId = "564d8003aca44f4f61850fcd";    // 掌阅热销榜
    private static String sSQHotRankId = "564d80457408cfcd63ae2dd0";    // 书旗热搜榜
    private static String sZHHotRankId = "54d430962c12d3740e4a3ed2";    // 纵横月票榜
    private static String sZLHotRankId = "5732aac02dbb268b5f037fc4";    // 逐浪点击榜
    private static String sBDHotRankId = "564ef4f985ed965d0280c9c2";    // 百度热搜榜
    public static final List<String> MALE_HOT_RANK_ID = Arrays.asList(sZYHotRankId,
            sSQHotRankId, sZHHotRankId, sZLHotRankId, sBDHotRankId);
    // 男生热门榜单的榜单名字
    public static final List<String> MALE_HOT_RANK_NAME = Arrays.asList("掌阅热销榜",
            "书旗热搜榜", "纵横月票榜", "逐浪点击榜", "百度热搜榜");

    // 女生热门榜单的榜单数
    public static final int FEMALE_HOT_RANK_NUM = 3;
    // 女生热门榜单的 id
    private static String sKHotRankId = "550b841715db45cd4b022107";    // 17K订阅榜
    private static String sNZYHotRankId = "564d80d0e8c613016446c5aa";    // 掌阅热销榜
    private static String sNSQHotRankId = "564d81151109835664770ad7";    // 书旗热搜榜
    public static final List<String> FEMALE_HOT_RANK_ID = Arrays.asList(
            sKHotRankId, sNZYHotRankId, sNSQHotRankId);
    // 男生热门榜单的榜单名字
    public static final List<String> FEMALE_HOT_RANK_NAME = Arrays.asList(
            "17K订阅榜", "掌阅热销榜", "书旗热搜榜");

    /* 错误信息 */
    // 小说源 api 没有找到相关小说
    public static final String NOT_FOUND_NOVELS = "没有找到相关小说";
    // 没有获取到相关目录信息
    public static final String NOT_FOUND_CATALOG_INFO = "没有找到相关目录";

    /* 数据库相关 */
    // 数据库名
    public static final String DB_NAME = "FReader.db";
    // 历史记录表
    public static final String TABLE_HISTORY = "TABLE_HISTORY";
    // 历史记录表的记录
    public static final String TABLE_HISTORY_ID = "TABLE_HISTORY_ID";       // 自增 id
    public static final String TABLE_HISTORY_WORD = "TABLE_HISTORY_WORD";   // 搜索词

    // 测试小说内容
    public static final String NOVEL_CONTENT = "小册子\n" +
            "少女露丝的心情 外传小册\n" +
            "露丝绝对是个天才没错，不过她的天赋集中在情报搜集以及分析。虽然可以让事物得以精确而且更有效率地运作进行，却不擅长可蓝或是奇莉华那样亲自拟定作战计划。更进一步的\n" +
            "说法就是过于保守，欠缺灵光一闪的特质。\n" +
            "“......我的料理真的可以满足将军大人吗？”\n" +
            "“露丝，怎么突然说这种话？”\n" +
            "“因为我不擅长研发全新的菜单或料理方法，心里多少有些不安，所以......”\n" +
            "说到这里，露丝难为情地低下头。这时她的烦恼正是自己每天做的料理是否合孝太郎的胃口。露丝的厨艺相当高明，由于她能让事物得以精确而且更有效率地运作进行，在料理方面当然也不例外。\n" +
            "不过也因为如此，她总是忠实地遵从食谱的做法，从未想过发明独特的特色，因此每次的口味都完全一样。这样孝太郎会不会吃腻呢？这就是露丝陷入苦恼的原因。\n" +
            "“这里又不是餐厅，我反而比较喜欢这样。”\n" +
            "“真的吗？”\n" +
            "“若变得跟餐厅一样，不就会让人静不下心了么？”\n" +
            "对孝太郎来说，他比较想在家里吃到熟悉的味道。只要走出家门，到哪都会吃到不熟悉的口味，因此他希望可以在家里尝到令人安心的料理。就这层意义而言，露丝的料理可说是近乎完美，令孝太郎大为满足。\n" +
            "“令人安心的料理......这就是将军大人对我的要求么？”\n" +
            "“呃......算是吧。不过若偶尔出现一些变化，例如采用高等食材之类的料理，我也欣然接受就是了。”\n" +
            "“将军大人……”\n" +
            "露丝闻言，突然莫名其妙的流下眼泪。孝太郎见状顿时慌了手脚，还以为自己说错了什么。\n" +
            "“露丝，你怎么哭了？我说错了什么吗？” \n" +
            "“不，不是这样的，因为听到将军大人吃了我的料理会感到安心，我只是太高兴了而已。”\n" +
            "“你烦恼到这种程度啊？”\n" +
            "“……这种感觉将军大人应该无法体会吧，呵呵……”\n" +
            "孝太郎的解释虽然仅止于表面，不过事实上露丝真正的想法要更深一层。想要在家享用露丝那种令人安心的料理，代表孝太郎接受了露丝住在家里的事实。简而言之，在孝太郎的认知中，露丝的料理就是所谓家常菜。孝太郎在解释时虽然没意识到这点，然而若内心深处没有这种渴望，也不会说出那种话，由于露丝对自己的料理深感不安，这对她来说无疑是莫大的肯定，因此才会忍不住哭了出来。\n" +
            "“那么将军大人，今晚想吃些什么？”\n" +
            "拭去泪水之后，露丝展露欢颜。双眼虽然依旧红肿，但当初并不是因为难过才掉泪，此时的笑容看起来格外灿烂。是令人不禁深深受到吸引的幸福笑容。\n" +
            "“……”\n" +
            "“将军大人？”\n" +
            "“啊，没事......好久没吃可乐饼了，突然想尝一尝。早苗她们虽然也经常做可乐饼，不过好像很久没吃到制作成功的可乐饼了。”\n" +
            "“好的，交给我吧，将军大人！ ”\n" +
            "露丝开始在脑中搜寻可乐饼的食谱。既然孝太郎都那么说了，代表他想吃的是标准囗味的可乐饼。不过光是这样还少了点乐趣，除了标准口味的可乐饼之外，显然还需要另外一道料理。露丝早已针对孝太郎的喜好进行详细的情报搜集以及分析，很快就做出了结论，而这也正是露丝独特的爱情表现。";
}
