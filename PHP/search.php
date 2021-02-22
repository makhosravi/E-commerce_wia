<?php
require('confiq.php');
$json = file_get_contents('php://input');
//$json = '{"searchText" : "9156654850"}';
$obj = json_decode($json);
$txt = $obj->{'searchText'};
$sql = "SELECT * FROM advertisment WHERE  adTitle LIKE '%$txt%' OR adExplain LIKE '%$txt%' ORDER BY id DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while ($row = $result->fetch_assoc()) {
        if($row["recActive"]=="Active")
        {
            $obj = json_decode($row["image"], TRUE);
            $img = array(
                "image1" => $obj[0],
            );
            $data[] = array(
                'id' => $row["id"],
                'userId' => $row["userId"],
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