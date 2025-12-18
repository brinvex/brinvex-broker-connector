# Brinvex Broker Connector

_Brinvex Broker Connector_ simplifies portfolio tracking by collecting 
transactions and daily asset values from banks and brokers. 
It supports online fetching as well as manual input.
Fetched data is stored locally to prevent repeated requests and improve performance.

### Contact
If you have any questions, feedback, or need assistance, please reach out to _info@brinvex.com_.
I am also open to exploring work partnerships of any kind. Whether you’re interested in collaboration,
integration, or other opportunities, feel free to get in touch - I’d love to hear from you!

### Maven Setup
It's not necessary to include all dependencies.
You only need to import the ones relevant to your specific use case.
For example, if you need integration with IBKR, 
import the ``brinvex-broker-connector-ibkr`` dependencies.

    <properties>
         <brinvex-broker-connector.version>1.2.9</brinvex-broker-connector.version>
    </properties>
    
    <repository>
        <id>github-pubrepo-brinvex</id>
        <name>Github Public Repository - Brinvex</name>
        <url>https://github.com/brinvex/brinvex-pubrepo/raw/main/</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>

    <dependency>
        <groupId>com.brinvex</groupId>
        <artifactId>brinvex-broker-connector-core</artifactId>
        <version>${brinvex-broker-connector.version}</version>
    </dependency>

    <!-- Optional IBKR connector-->
    <dependency>
        <groupId>com.brinvex</groupId>
        <artifactId>brinvex-broker-connector-ibkr</artifactId>
        <version>${brinvex-broker-connector.version}</version>
    </dependency>

    <!-- Optional RVLT connector -->
    <dependency>
        <groupId>com.brinvex</groupId>
        <artifactId>brinvex-broker-connector-rvlt</artifactId>
        <version>${brinvex-broker-connector.version}</version>
    </dependency>

    <!-- Optional FIOB connector -->
    <dependency>
        <groupId>com.brinvex</groupId>
        <artifactId>brinvex-broker-connector-fiob</artifactId>
        <version>${brinvex-broker-connector.version}</version>
    </dependency>

    <!-- Optional AMND connector -->
    <dependency>
        <groupId>com.brinvex</groupId>
        <artifactId>brinvex-broker-connector-amnd</artifactId>
        <version>${brinvex-broker-connector.version}</version>
    </dependency>


### Requirements

Java 25 or above

### License

The _Brinvex Broker Connector_ is released under version 2.0 of the Apache License.

## Practical insights and tips

#### IBKR - Symbol Discrepancy in Stock Position

A discrepancy may be observed in the display symbols for stock positions,
such as those in the German company Siemens (ISIN: DE0007236101).
When purchased on IBIS, the symbol for Siemens stock may appear as "SIE" in the TWS platform 
and the Portfolio screen of the Interactive Brokers web application.
However, in report statements, including flex statements, the symbol may be listed as "SIEd."  
This inconsistency arises because reports use the primary exchange symbol, 
which may differ from the one displayed in other parts of the platform. 
Despite this variation, the stock remains the same, as confirmed by matching ISINs and other details. 
This behavior is a standard design feature of the IBKR platform.  
Starting in 2024, reports have filled not only the attribute _symbol_ but also _underlyingSymbol_, 
which (for Stocks and ETFs) provides a symbol we prefer for further processing.
For old items, underlyingSymbol remains blank even when generating fresh reports for past periods. 
In these cases, the old _symbol_ field is used, but to make it consistent 
we do some reconciliation in code. It affects only few instruments: 
- _CSPX.DE -> SXR8.DE_ 
- _SIEd -> SIE.DE_ 

#### IBKR - Delayed Update Effect

Many updates or changes made by the user will only take full effect after one business day. 
For example, when modifying the _Account Alias_, the new value will not immediately appear in the statements. 
Instead, the old value will continue to be displayed until the system processes the update overnight. 
Users should expect this one-business-day period for updates to be fully reflected.

#### IBKR - Account ID change
On August 1, 2024, Interactive Brokers Ireland Limited (IBIE) and 
Interactive Brokers Central Europe Zrt. (IBCE) merged into a single entity. 
All former IBCE clients were moved to IBIE, and they got new Account IDs under IBIE.  
https://www.ibkrguides.com/kb/merger-of-two-eu-broker-dealers.htm
If such a scenario occurs again, be aware that IBKR reports may appear 
inconsistent and unstable for a period. It is advisable to wait a few days 
before using them, allowing time for reports of old accounts to stabilize 
and new account reports to include all important data.
In 2024, a 14-day waiting period was sufficient for the reports to consolidate.

#### IBKR – Fictitious FX_BUY Trade on the Date a Foreign-Currency Dividend Is Received
The Activity Statement shows an _FX_BUY Trade_ (buy EUR, sell USD) 
on the same date a USD-denominated dividend is received. 
This trade does not affect the cash balance in either currency.
On that same date, the report also shows a transfer of type _"Adjustment: Cash Receipt/Disbursement/Transfer"_ 
that reverses the FX_BUY trade.
To keep cash balances accurate, one should always process both entries together.

#### IBKR - Symbol-Only Changes Not Reported as Corporate Actions
IBKR does not record symbol-only changes 
(where the underlying financial instrument remains the same but the ticker changes) 
as explicit Corporate Actions in Flex Queries. When a ticker is renamed, 
the old symbol simply disappears from reports and the new one appears in its place.
To address this limitation, we leverage the PriorPeriodPosition section
to compare consecutive position snapshots. By matching stable identifiers (e.g., figi or isin)
across dates, we can detect cases where the symbol changes 
and record these as synthetic “transformation transactions” in our systems. 
When a Flex Query is generated for a full year, 
it produces a daily PriorPeriodPosition snapshot - meaning up to 365 entries 
for each position - which we process to identify such symbol changes efficiently.