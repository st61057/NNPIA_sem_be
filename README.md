## NNPIA semestrální práce - backend

Spring boot aplikace. Slouží jako REST API interface pro React frontend.

#### Téma
Semestrální práce se zabývala vytvořením aplikace - rezervační systém pro barbershop
Hlavním úkolem bylo vytvořit aplikaci, která umožní anonymnímu uživateli vytvořit rezervaci v barbershopu.
Přihlášený uživatel (pracovník barbershopu) může spravovat existující rezervace, spravovat dostupné procedury, které jsou nabízeny.

#### Kód
Kód je rozdělen do následujících balíčků
- config - Konfigurační soubory
- controller - API rozhranní
- dto - Datové modely
- entity - Datové entity
- enums - Výčtové typy
- repository - JPA repositories
- service - Servisní třídy

#### Služby:

/public - dostupné bez autentizace, převážně zobrazování dostupných informací pro uživatele, prioritně slouží k vytváření rezervací

/auth - autentikace k přihlášení

/api - dostupné pouze s autentizací, k práci a spravování rezervací a procedur
