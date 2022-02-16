import com.hugovs.isim.model.client.Client;
import com.hugovs.isim.model.exchange.Companies;
import com.hugovs.isim.model.exchange.Database;
import com.hugovs.isim.model.exchange.Historic;
import com.hugovs.isim.model.ia.investors.ZeInvestor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: hugo_<br/>
 * Date: 29/04/2018<br/>
 * Time: 01:26<br/>
 */
public class ZeMain {

    public static void main(String[] args) throws ParseException {

        Date finalDate = (new SimpleDateFormat("dd/MM/yy").parse("31/12/16"));
        finalDate.setHours(1); // Ajustar horário de verão

        Client client = new Client(100000.00f);
        Historic knowledge = Database.ALL_2014_2015;


    }

}
