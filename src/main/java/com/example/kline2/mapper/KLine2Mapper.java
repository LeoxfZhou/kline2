package com.example.kline2.mapper;

import com.example.kline2.domain.KLine2;

import org.apache.ibatis.annotations.*;
import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface KLine2Mapper {


    @Results(id = "klineObject", value = {
            @Result(property = "openTime", column = "open_time"),
            @Result(property = "openPrice", column = "open_price"),
            @Result(property = "highPrice", column = "high_price"),
            @Result(property = "lowPrice", column = "low_price"),
            @Result(property = "volume", column = "volume"),
            @Result(property = "closePrice", column = "close_price"),
            @Result(property = "quoteAssetVolume", column = "quote_asset_volume"),
            @Result(property = "closeTime", column = "close_time"),
            @Result(property = "numOfTrades", column = "num_of_trades"),
            @Result(property = "takerBuyBaseAssetVolume", column = "taker_buy_base_asset_volume"),
            @Result(property = "takerBuyBaseQuoteAssetVolume", column = "taker_buy_base_quote_asset_volume"),
            @Result(property = "unused", column = "unused"),
            @Result(property = "symbol", column = "symbol"),
    })

    @Select("SELECT * FROM kline2")
    public List<KLine2> findAllKlines();

    @Select("SELECT COUNT(*) FROM kline2 WHERE open_time = #{openTime} AND close_time = #{closeTime} AND symbol = #{symbol}")
    Integer findExistingKLine(@Param("openTime") BigDecimal openTime, @Param("closeTime") BigDecimal closeTime, @Param("symbol") String symbol);

    // todo save fcuntion kline
    /*@Insert("INSERT INTO kline2 " +
            "(open_time, open_price, high_price, low_price, close_price, volume, " +
            "quote_asset_volume, close_time, num_of_trades, taker_buy_base_asset_volume, " +
            "taker_buy_base_quote_asset_volume, unused, symbol) " +
            "VALUES (#{openTime}, #{openPrice}, #{highPrice}, #{lowPrice}, #{closePrice}, #{volume}, " +
            "#{quoteAssetVolume}, #{closeTime}, #{numOfTrades}, #{takerBuyBaseAssetVolume}, " +
            "#{takerBuyBaseQuoteAssetVolume}, #{unused}, #{symbol})")
    int insertKline(KLine2 kline);

    @Select("SELECT * FROM kline2 WHERE open_time = #{openTime} AND symbol = #{symbol}")
    @ResultMap("klineObject")
    KLine2 findKlineByOpenTimeAndSymbol(@Param("openTime") BigDecimal openTime, @Param("symbol") String symbol);*/

    @Insert({
            "<script>",
            "INSERT INTO kline2 (open_time, open_price, high_price, low_price, volume, close_price, ",
            "quote_asset_volume, close_time, num_of_trades, taker_buy_base_asset_volume, ",
            "taker_buy_base_quote_asset_volume, unused, symbol) ",
            "VALUES ",
            "<foreach collection='klineList' item='kline' separator=','>",
            "(#{kline.openTime}, #{kline.openPrice}, #{kline.highPrice}, #{kline.lowPrice}, ",
            "#{kline.volume}, #{kline.closePrice}, #{kline.quoteAssetVolume}, #{kline.closeTime}, ",
            "#{kline.numOfTrades}, #{kline.takerBuyBaseAssetVolume}, #{kline.takerBuyBaseQuoteAssetVolume}, ",
            "#{kline.unused}, #{kline.symbol})",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("klineList") List<KLine2> klineList);



    @Delete("DELETE FROM kline2 WHERE symbol = #{symbol}")
    void deleteKlineBySymbol(String symbol);
}
