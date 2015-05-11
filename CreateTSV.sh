#!/bin/bash

links="http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/amsterdammuseum_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/bbcwildlife_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/bookmashup_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/bricklink_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/cordis_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/dailymed_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/dblp_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/dbtune_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/diseasome_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/drugbank_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/eunis_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/eurostat_linkedstatistics_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/eurostat_wbsg_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/factbook_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/flickrwrappr_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/gadm_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/geospecies_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/gho_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/gutenberg_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/italian_public_schools_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/linkedgeodata_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/linkedmdb_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/musicbrainz_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/nytimes_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/opencyc_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/openei_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/revyu_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/sider_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/tcm_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/umbel_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/uscensus_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/wikicompany_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/wordnet_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/links/yago_links.nt.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/geonames_links_en_en.ttl.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/freebase_links_en.ttl.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/interlanguage_links_chapters_en.ttl.bz2
http://data.dws.informatik.uni-mannheim.de/dbpedia/2014/en/interlanguage_links_en.ttl.bz2"

#Remove previous loaded files, just for a clean up.
rm -f ld.tsv

#Import all triples into the file ld.tsv (Tab Separeted Value).
#This commands already treat duplicates and proper formating.
for file in "$links"
do
    curl $file | bzcat | grep "\#sameAs" |  rapper -i ntriples -I - - file  2>/dev/null | cut -f1,3 -d '>' | sed 's/> </\t/g' | sed 's/> .&//g' | sed 's/^<//g' > temp.storage
grep "^http://dbpedia.org" temp.storage > correct.tsv
grep -V "^http://dbpedia.org" temp.storage | cut -f 2,1 >> correct.tsv
cat correct.tsv

done > ld.tsv
