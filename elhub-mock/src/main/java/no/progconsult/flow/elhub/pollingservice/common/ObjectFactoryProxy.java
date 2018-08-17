package no.progconsult.flow.elhub.pollingservice.common;

import no.elhub.emif.acknowledgement.v1.Acknowledgement;
import no.elhub.emif.market.confirmendofsupply.v1.ConfirmEndOfSupply;
import no.elhub.emif.market.confirmstartofsupply.v1.ConfirmStartOfSupply;
import no.elhub.emif.market.notifyendofsupply.v1.NotifyEndOfSupply;
import no.elhub.emif.market.notifystartofsupply.v1.NotifyStartOfSupply;
import no.elhub.emif.market.rejectendofsupply.v1.RejectEndOfSupply;
import no.elhub.emif.market.rejectstartofsupply.v1.RejectStartOfSupply;
import no.elhub.emif.market.requestendofsupply.v1.RequestEndOfSupply;
import no.elhub.emif.market.requeststartofsupply.v1.ObjectFactory;
import no.elhub.emif.market.requeststartofsupply.v1.RequestStartOfSupply;
import no.elhub.emif.masterdata.notifycustomerinformation.v1.NotifyCustomerInformation;
import no.elhub.emif.masterdata.notifymeteringpointcharacteristics.v1.NotifyMeteringPointCharacteristics;
import no.elhub.emif.masterdata.requestupdatecustomerinformation.v1.RequestUpdateCustomerInformation;
import no.elhub.emif.masterdata.requestupdatemasterdatameteringpoint.v1.RequestUpdateMasterDataMeteringPoint;
import no.elhub.emif.metering.notifyvalidateddataforbillingenergy.v1.NotifyValidatedDataForBillingEnergy;
import no.elhub.emif.metering.pricevolumecombinationforreconciliation.v1.PriceVolumeCombinationForReconciliation;
import no.elhub.emif.metering.requestcollecteddata.v1.RequestCollectedData;
import no.elhub.emif.necs.consumptiontonecs.v1.ConsumptionToNECS;
import no.elhub.emif.query.portfoliooverview.v1.PortfolioOverview;
import no.elhub.emif.query.requesttogridaccessprovider.v1.RequestToGridAccessProvider;
import no.elhub.emif.query.responsefromgridaccessprovider.v1.ResponseFromGridAccessProvider;

import javax.xml.bind.JAXBElement;

/**
 * Proxy for Jaxb ObjectFactories.
 *
 * @author bno
 */
public class ObjectFactoryProxy {

    public static JAXBElement<ResponseFromGridAccessProvider> wrap(
            final ResponseFromGridAccessProvider responseFromGridAccessProvider) {
        return new no.elhub.emif.query.responsefromgridaccessprovider.v1.ObjectFactory()
                .createResponseFromGridAccessProvider(responseFromGridAccessProvider);
    }

    public static JAXBElement<RequestToGridAccessProvider> wrap(final RequestToGridAccessProvider requestToGridAccessProvider) {
        return new no.elhub.emif.query.requesttogridaccessprovider.v1.ObjectFactory()
                .createRequestToGridAccessProvider(requestToGridAccessProvider);
    }

    public static JAXBElement<NotifyMeteringPointCharacteristics> wrap(
            final NotifyMeteringPointCharacteristics notifyMeteringPointCharacteristics) {
        return new no.elhub.emif.masterdata.notifymeteringpointcharacteristics.v1.ObjectFactory()
                .createNotifyMeteringPointCharacteristics(notifyMeteringPointCharacteristics);
    }

    public static JAXBElement<NotifyEndOfSupply> wrap(final NotifyEndOfSupply notifyEndOfSupply) {
        return new no.elhub.emif.market.notifyendofsupply.v1.ObjectFactory()
                .createNotifyEndOfSupply(notifyEndOfSupply);
    }

    public static JAXBElement<RequestUpdateCustomerInformation> wrap(
            final RequestUpdateCustomerInformation requestUpdateCustomerInformation) {
        return new no.elhub.emif.masterdata.requestupdatecustomerinformation.v1.ObjectFactory()
                .createRequestUpdateCustomerInformation(requestUpdateCustomerInformation);
    }

    public static JAXBElement<ConfirmStartOfSupply> wrap(final ConfirmStartOfSupply confirmStartOfSupply) {
        return new no.elhub.emif.market.confirmstartofsupply.v1.ObjectFactory()
                .createConfirmStartOfSupply(confirmStartOfSupply);
    }

    public static JAXBElement<RequestUpdateMasterDataMeteringPoint> wrap(
            final RequestUpdateMasterDataMeteringPoint requestUpdateMasterDataMeteringPoint) {
        return new no.elhub.emif.masterdata.requestupdatemasterdatameteringpoint.v1.ObjectFactory()
                .createRequestUpdateMasterDataMeteringPoint(requestUpdateMasterDataMeteringPoint);
    }

    public static JAXBElement<Acknowledgement> wrap(final Acknowledgement acknowledgement) {
        return new no.elhub.emif.acknowledgement.v1.ObjectFactory()
                .createAcknowledgement(acknowledgement);
    }

    public static JAXBElement<RequestStartOfSupply> wrap(final RequestStartOfSupply requestStartOfSupply) {
        return new ObjectFactory().createRequestStartOfSupply(requestStartOfSupply);
    }

    public static JAXBElement<PortfolioOverview> wrap(final PortfolioOverview portfolioOverview) {
        return new no.elhub.emif.query.portfoliooverview.v1.ObjectFactory()
                .createPortfolioOverview(portfolioOverview);
    }

    public static JAXBElement<RejectStartOfSupply> wrap(final RejectStartOfSupply rejectStartOfSupply) {
        return new no.elhub.emif.market.rejectstartofsupply.v1.ObjectFactory()
                .createRejectStartOfSupply(rejectStartOfSupply);
    }

    public static JAXBElement<RequestEndOfSupply> wrap(final RequestEndOfSupply requestEndOfSupply) {
        return new no.elhub.emif.market.requestendofsupply.v1.ObjectFactory()
                .createRequestEndOfSupply(requestEndOfSupply);
    }

    public static JAXBElement<NotifyStartOfSupply> wrap(final NotifyStartOfSupply notifyStartOfSupply) {
        return new no.elhub.emif.market.notifystartofsupply.v1.ObjectFactory()
                .createNotifyStartOfSupply(notifyStartOfSupply);
    }

    public static JAXBElement<ConfirmEndOfSupply> wrap(final ConfirmEndOfSupply confirmEndOfSupply) {
        return new no.elhub.emif.market.confirmendofsupply.v1.ObjectFactory()
                .createConfirmEndOfSupply(confirmEndOfSupply);
    }

    public static JAXBElement<NotifyCustomerInformation> wrap(final NotifyCustomerInformation notifyCustomerInformation) {
        return new no.elhub.emif.masterdata.notifycustomerinformation.v1.ObjectFactory()
                .createNotifyCustomerInformation(notifyCustomerInformation);
    }

    public static JAXBElement<RejectEndOfSupply> wrap(final RejectEndOfSupply rejectEndOfSupply) {
        return new no.elhub.emif.market.rejectendofsupply.v1.ObjectFactory()
                .createRejectEndOfSupply(rejectEndOfSupply);
    }

    public static JAXBElement<RequestCollectedData> wrap(final RequestCollectedData requestCollectedData) {
        return new no.elhub.emif.metering.requestcollecteddata.v1.ObjectFactory()
                .createRequestCollectedData(requestCollectedData);
    }

    public static JAXBElement<PriceVolumeCombinationForReconciliation> wrap(
            final PriceVolumeCombinationForReconciliation priceVolumeCombinationForReconciliation) {
        return new no.elhub.emif.metering.pricevolumecombinationforreconciliation.v1.ObjectFactory()
                .createPriceVolumeCombinationForReconciliation(priceVolumeCombinationForReconciliation);
    }

    public static JAXBElement<NotifyValidatedDataForBillingEnergy> wrap(
            final NotifyValidatedDataForBillingEnergy notifyValidatedDataForBillingEnergy) {
        return new no.elhub.emif.metering.notifyvalidateddataforbillingenergy.v1.ObjectFactory()
                .createNotifyValidatedDataForBillingEnergy(notifyValidatedDataForBillingEnergy);
    }

    public static JAXBElement<ConsumptionToNECS> wrap(final ConsumptionToNECS consumptionToNECS) {
        return new no.elhub.emif.necs.consumptiontonecs.v1.ObjectFactory()
                .createConsumptionToNECS(consumptionToNECS);
    }
}
