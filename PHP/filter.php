<?php

    require_once('config.php');


    $json = file_get_contents('php://input');
    //$json='{"area":"10 - 200000","trust":null,"orderType":null,"isEmergency":null,"isCountrySide":null,"isImage":null,"numOfRoom":null,"price":"1000 - 2000000000","officialDoc":null,"rent":null,"categoryId":"11","advertiserType":null,"page":1}';
    $obj = json_decode($json, TRUE);
    //var_dump($obj);
    $area = $obj{'area'};
    $price = $obj{'price'};
    $orderType = $obj{'orderType'};
    $advertiserType = $obj{'advertiserType'};
    $numOfRoom = $obj{'numOfRoom'};
    $isCountrySide = $obj{'isCountrySide'};
    $trust = $obj{'trust'};
    $rent = $obj{'rent'};
    $officialDoc = $obj{'officialDoc'};
    $categoryId = $obj{'categoryId'};
    $image = $obj{'image'};
    $isEmergency = $obj{'isEmergency'};
    $page = $obj{'page'};

    if ($categoryId == 0) 
    {
        $sql = "SELECT * FROM advertisement WHERE categoryId LIKE '%'";
    }
    else{
        $sql = "SELECT * FROM advertisement WHERE categoryId LIKE '$categoryId%'";
    }
    if ($area != "") {
        $area = explode(" - ", $area);
        $sql = $sql . " AND area BETWEEN $area[0] AND $area[1]";
    }
    if ($price != "") {
        $price = explode(" - ", $price);
        $sql = $sql . " AND price BETWEEN $price[0] AND $price[1]";
    }
    if ($orderType != "") {
        $sql = $sql . " AND orderType = '$orderType'";
    }
    if ($advertiserType != "") {
        $sql = $sql . " AND advertiserType = '$advertiserType'";
    }
    if ($numOfRoom != "") {
        $sql = $sql . " AND numOfRoom = '$numOfRoom'";
    }
    if ($isCountrySide != "") {
        $sql = $sql . " AND isCountrySide = '$isCountrySide'";
    }
    if ($trust != "") {
        $sql = $sql . " AND trust = '$trust'";
    }
    if ($rent != "") {
        $sql = $sql . " AND rent = '$resnt'";
    }
    if ($officialDoc != "") {
        $sql = $sql . " AND officialDoc = '$officialDoc'";
    }
    if ($image != "") {
        $sql = $sql . " AND NOT image = 'NO_IMAGE'";
    }
    if ($isEmergency != "") {
        $sql = sql. " AND isEmergency = '$isEmergency'";
    }
    //echo $sql
    $page = $page - 1;
    $pages = $page * 10;
    //LIMIT 10,2
    $sql = $sql . " ORDER BY id DESC";
    $sql = $sql . " LIMIT $pages,10";
    $result = $conn->query($sql);

    if ($result->num_row > 0) {
        // output of each row
        while ($row = $result->fetch_assoc()) {
            if ($row["recActive"]=="Active") 
            {
                $obj = json_decode($row["image"], TRUE);
                $img = array(
                    "image1" => $obj[0], 
                    "image2" => $obj[1],
                    "iamge3" => $obj[2],
                    "image4" => $obj[3]
                );
                $data[] = array(
                    'id' => $row["id"],
                    'price' => $row["price"],
                    'image' => $img,
                    'adTitle' => $row["adTitle"],
                    'adDate' => $row["adDate"],
                    'isEmergency' => $row["isEmergency"]
                );
            }
        }
    }
    else {
        $logObj[] = array(
            'state' => "2", 
        );
        $check = 1;
        $logJson = json_encode($logObj);
        echo $logJson;
    }
    if ($check != 1) {
        echo json_encode($data);
    }
    $conn->close();
?>