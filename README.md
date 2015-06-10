# Sensor Module
Modul wysyłajacy informacje na temat danego komputera do zdalnego serwera (ActiveMQ).


# Instalacja 
ant version >= 1.8.2

* `install.properties` - plik konfiguracyjny, można w nim ustawić: 

`app_dir` - katalog , gdzie ma zostać wypakowany serwer aplikacyjny wraz z aplikacją (domyślnie: "/monitor-back")

`app_java_home` - środowisko jdk dla aplikacji (wymagana wersja jdk: **1.8**, domyślnie: "C:/Program Files/Java/jdk1.8.0_25")

* po instalacji w katalogu `app_dir` pojawi się dodatkowy folder `/sensor` z serverem aplikacyjnym oraz aplikacją, dodatkowo pojawią się też skrypt do uruchomienia sensora.


Po uruchomieniu sensora dane wysylane są do kolejki activemq.


# Ustawiania ActiveMq

* 'client.properties' - plik w którym można ustawic 

'Schedule' - co ile sekund maja zostac wyslane dane 

'Delay' - przerwa miedzy wyslka

'JMSUSername' - nazwa usernama dla JMSa

'JMSPaswword' - haslo dla JMSa

'JMSURL' - adres activemq 

'JMSQueueName' - nazwa kolejki 


JSON:
{
    "host": {
        "hostname": "H9470305442550",
        "ip": "192.168.0.1"
    },
    "name": null,
    "measurement": {
        "mem": {
            "total": 17036742656,
            "ram": 16248,
            "used": 6842998784,
            "actualUsed": 6280744960
        },
        "cpu": {
            "user": 59159571,
            "sys": 32398534,
            "nice": 0,
            "idle": 558849198,
            "wait": 0,
            "irq": 2286286,
            "softIrq": 0,
            "stolen": 0,
            "total": 1626019373
        },
        "network": {
            "mac": null,
            "ip": null,
            "stat": null
        },
        "disk": {
            "read": 0,
            "write": 0
        }
    }
}
