# Progetto Orario lezioni.

L'orario delle lezioni di una scuola o di corsi universitari è spesso redatto da una persona specifica, che in questa descrizione sarà indicata con il nome di scheduler. Lo scheduler prende in considerazione le esigenze del corpo docente e definisce il calendario delle lezioni. Dopo averlo definito vi è un periodo di ‘assestamento’ in cui ogni docente propone delle modifiche per motivi diversi. 

Il caso di studio prevede la realizzazione di un’app che supporti lo scheduler nella redazione dell'orario utilizzando il modello crowd. Facendo cioè in modo che siano i docenti a proporre gli slot di indisponibilità, di potenziale indisponibilità mediante l’uso di un dispositivo mobile, in modo da inserire dei vincoli da rispettare nella definizione dell’orario. 

L’app semplifica questo processo facendo sì che la richiesta di Buono sia inviata a De Carolis e se quest’ultima accetta è inviata una richiesta allo scheduler che, dopo aver visionato e convalidato il cambio, consente all’app di aggiornare l’orario con il cambio per la settimana interessata. Una volta effettuato il cambio orario, il vecchio orario è memorizzato per mantenere lo storico dei cambi.

Condizione necessaria per far funzionare l’app è la gestione del calendario che effettua lo scheduler e i docenti, questi ultimi limitatamente ai corsi di cui sono titolari.  Per semplificare le cose e tenendo conto che l’orario scolastico/uniersitario hanno una struttura settimanale, l’app deve agevolare la creazione dell’orario settimanale da parte dello scheduler e consentire ai professori l’inserimento delle loro indisponibilità settimanale anche prima che l’orario venga definito. La fase iniziale di inserimento delle indisponibilità è chiamata fase di bidding.
