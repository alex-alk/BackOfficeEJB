package backoffice;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;

@MessageDriven(mappedName = "jms/queue/DLQ", activationConfig= {
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/DLQ")
})
public class BackofficeBean implements MessageListener{
    @EJB(lookup="java:module/PaymentRecorderImpl")
    private PaymentRecorder recorder;

    public BackofficeBean() {
    }
    @Override
    public void onMessage(Message incoming) {
       try {
            if (incoming instanceof TextMessage) {
                TextMessage message = (TextMessage) incoming;
                this.recorder.recordPayment(message.getText());
            } else {
                System.out.println("Unexpected message type: " + incoming.getClass().getName());
            }
        } catch (JMSException jmse) {
            System.out.println("Exception recording incoming message: " + jmse.getMessage());
        }
    
    }
}
