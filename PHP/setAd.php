<?php
    $servername = "localhost:3306";
    $username = "serverte_makh";
    $password = "1342makh1342";
    $dbname = "serverte_divar";
    // create connection
    $conn = new mysqli($servername, $username, $password, $dbname);
    // check connection
    if($conn->connect_error)
    {
        die("Connection failed : " . $conn->connect-error);
    }

    if(!$conn->set_charset("utf8"))
    {
        printf("Error loading character set utf8", $mysqli->error);
    }
    else
    {
        $conn->character_set_name();
    }

    $json = file_get_contents('php://input');
    $obj = json_decode($json, TRUE);
    // var_dump($obj);
    $userPhone = $obj{'userPhone'};
    $categoryId = $obj{'categoryId'};
    $city = $obj{'userCity'};
    $area = $obj{'area'};
    $price = $obj{'price'};
    $orderType = $obj{'orderType'};
    $advertiserType = $obj{'advertiserType'};
    $numOfRoom = $obj{'numOfRoom'};
    $isCountrySide = $obj{'isCountrySide'};
    $trust = $obj{'trust'};
    $rent = $obj{'rent'};
    $officialDoc = $obj{'officialDoc'};
    $image = $obj{'image'};
    // print_r($image[0]['img1']);
    // var_dump($image['img1']);
    $imgCount = count($obj{'image'});
    // $tempImage = $obj{'image'}{'img1'};
    $adTitle = $obj{'adTitle'};
    $adExplain = $obj{'adExplain'};
    $lifeTime = $obj{'lifeTime'};
    $isEmergency = $obj['isEmergency'];
    $sql = "SELECT * FROM user WHERE phoneNumber = '$userPhone' ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0)
    {
        // output data of each row
        while ($row = $result->fetch_assoc())
        {
            $userId = $row["id"];
        }           
    }
    if ($image != "NO_IMAGE") 
    {
        $tempImage[0] = $obj{'image'}{'img1'};
        $tempImage[1] = $obj{'image'}{'img2'};
        $tempImage[2] = $obj{'image'}{'img3'};
        $tempImage[3] = $obj{'image'}{'img4'};
        $tempImage[4] = $obj{'image'}{'img5'};
        for ($x = 0; $x < $imgCount; $x++) 
        { 
            $date = date("His");
            $temp = rand(10, 100);
            $name[$x] = "img".$temp."_".$userId."_".$date;
            $path = "uploads/$name[$x].png";
            file_put_contents($path,base64_decode($tempImage[$x]));
            $imageUrl[$x] = "http://www.servertest.ir/divar_wiadev/$path";
        }
        $imgJson = json_encode($imageUrl, JSON_FORCE_OBJECT);
    }
    else
    {
        $imgJson = "NO_IMAGE";
    }
    $adDate = date('Y-m-d\TH:i:s');
    $recActive = "Active";
    

    $sql = "INSERT INTO advertisement (userId,categoryId,city,area,price,
        orderType,advertiserType,numOfRoom,isCountrySide,trust,rent,
        officialDoc,image,adTitle,adDate,adExplain,lifeTime,isEmergency,recActive)
         VALUES ('$userId','$categoryId','$city','$area','$price','$orderType','$advertiserType',
         '$numOfRoom','$isCountrySide','$trust','$rent','$officialDoc','$imgJson','$adTitle','$adDate',
         '$adExplain','$lifeTime','$isEmergency','$recActive')";
    $logObj = new \stdClass();
    if ($conn->query($sql) === TRUE) 
    {
        $logObj->sate = 2;
    }
    else
    {
        $logObj->state = 3;
    }

    $logJson = json_encode($logObj);
    echo $logJson;
    $conn->close();
?>