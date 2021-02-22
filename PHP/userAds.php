<?php
    require('config.php');
    $json = file_get_contents('php://input');
    $obj = json_decode($json);
    $userPhone = $obj->{'userPhone'};

    $sql = "SELECT * FROM user WHERE phoneNumber = '$userPhone' ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            $userId = $row["id"];
        }
    }

    $sql = "SELECT * FROM advertisement WHERE userId = '$userId' ORDER BY id DESC ";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) 
    {
        while ($row = $result->fetch_assoc()) 
        {
            if ($row["recActive"] == "Active") 
            {
                $check = 1;
                $obj = json_decode($row["image"],TRUE);
                $img = array(
                    'image1' => $obj[0],
                    'image2' => $obj[1],
                    'image3' => $obj[2],
                    'image4' => $obj[3],
                );
                $data[] = array(
                    'id' => $row["id"],
                    'userId' => $userId,
                    'price' => $row["price"],
                    'image' => $img,
                    'adTitle' => $row["adTitle"],
                    'adDate' => $row["adDate"],
                    'isEmergency' => $row["isEmergency"],
                );
            }
        }
    }

    if($check != 1) {
        $logObj[] = array(
            'state' => "2",
        );
        $logJson = json_encode($logObj);
        echo $logJson;
    }
    else
    {
        echo json_encode($data);
    }

    $conn->close();
?>