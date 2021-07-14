import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HttpRequest {
    @GET("torgi/ajax_search/")
    Call<ResponseBody> getData(
            @Query("torgi[bidnumber]") String bidNumber,
            @Query("torgi[status]") String status,
            @Query("torgi[torgpublishdate][from]") String publishDateFrom,
            @Query("torgi[torgpublishdate][to]") String publishDateTo,
            @Query("torgi[propname]") String propName,
            @Query("torgi[region]") String region ,
            @Query("torgi[city]") String city,
            @Query("torgi[startprice][from]") String startPriceFrom,
            @Query("torgi[startprice][to]") String startPriceTo,
            @Query("torgi[torgexpiredate][from]") String xpireDateFrom,
            @Query("torgi[torgexpiredate][to]") String xpireDateTo);

}
