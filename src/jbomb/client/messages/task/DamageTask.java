package jbomb.client.messages.task;

import java.text.DecimalFormat;
import jbomb.client.game.ClientContext;
import jbomb.common.messages.DamageMessage;

public class DamageTask implements Task<DamageMessage> {

    private static final DecimalFormat DF = new DecimalFormat();

    static {
        DF.setMaximumFractionDigits(2);
    }

    @Override
    public void doThis(DamageMessage message) {
        ClientContext.APP.getHealtWithId((int) message.getId()).setText(DF.format(message.getDamage()) 
                + "%");
    }
}
