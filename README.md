# pensjon-pol-innsyn
Tjeneste for å kunne gi innsyn i pensjonsdata i henhold til [Lov om behandling av personopplysninger](https://lovdata.no/dokument/NL/lov/2018-06-15-38) (personopplysningsloven, POL).

Innsyn oppnås ved å gjøre et uttrekk av pensjonsdata og overføre dataene til en Excel-fil.

# Komme i gang

Request sendes til path `\innsyn`, med fødselsnummer i `fnr` header.  
For autentisering brukes STS token.

Svaret lagres som en fil i .xlsx format.

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan rettes mot:

* Mathias Sand Jahren

E-post: mathias.sand.jahren@nav.no

## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen [#samhandling_pensjonsområdet](https://nav-it.slack.com/archives/CQ08JC3UG).
