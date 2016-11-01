<?php
/**
 * Created by IntelliJ IDEA.
 * User: Venelin
 * Date: 1.11.2016 г.
 * Time: 02:32 ч.
 */

$word = trim(fgets(STDIN));
$text = trim(fgets(STDIN));

$regex = '/[!.?]/';
$wordPattern = '/\b' . $word . '\b/';
$sentences = preg_split($regex, $text);

foreach ($sentences as $sentence) {
    $match = preg_match(@$wordPattern, $sentence);
    if ($match){
        echo trim($sentence) . PHP_EOL;
    }
}
